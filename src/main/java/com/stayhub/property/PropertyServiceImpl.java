package com.stayhub.property;

import com.stayhub.common.exception.BusinessException;
import com.stayhub.common.exception.ResourceNotFoundException;
import com.stayhub.config.StorageProperties;
import com.stayhub.property.dto.AmenityResponse;
import com.stayhub.property.dto.PropertyCreateRequest;
import com.stayhub.property.dto.PropertyImageOrderRequest;
import com.stayhub.property.dto.PropertyImageResponse;
import com.stayhub.property.dto.PropertyResponse;
import com.stayhub.property.dto.PropertySummary;
import com.stayhub.property.dto.PropertyUpdateRequest;
import com.stayhub.storage.StoredFile;
import com.stayhub.storage.StorageService;
import com.stayhub.user.User;
import com.stayhub.user.UserRepository;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PropertyServiceImpl implements PropertyService {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png");
    private static final long MAX_IMAGE_PIXELS = 40_000_000L;

    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository imageRepository;
    private final AmenityRepository amenityRepository;
    private final UserRepository userRepository;
    private final PropertyMapper mapper;
    private final StorageService storageService;
    private final StorageProperties storageProperties;

    @Override
    public PropertyResponse getPublicProperty(Long propertyId) {
        Property property = propertyRepository.findByIdAndStatus(propertyId, PropertyStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        return mapper.toResponse(property);
    }

    @Override
    public PropertyResponse getHostProperty(Long hostId, Long propertyId) {
        return mapper.toResponse(findOwnedProperty(hostId, propertyId));
    }

    @Override
    public PropertyUpdateRequest getHostPropertyForEdit(Long hostId, Long propertyId) {
        return mapper.toUpdateRequest(findOwnedProperty(hostId, propertyId));
    }

    @Override
    public List<PropertySummary> getHostProperties(Long hostId) {
        return propertyRepository.findAllByHostIdOrderByUpdatedAtDesc(hostId).stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public List<AmenityResponse> getAmenities() {
        return amenityRepository.findAllByOrderByNameAsc().stream()
                .map(mapper::toAmenityResponse)
                .toList();
    }

    @Override
    @Transactional
    public PropertyResponse createProperty(Long hostId, PropertyCreateRequest request) {
        User host = userRepository.findById(hostId)
                .orElseThrow(() -> new ResourceNotFoundException("Host not found"));
        Property property = Property.builder()
                .host(host)
                .status(PropertyStatus.DRAFT)
                .ratingAvg(BigDecimal.ZERO)
                .build();
        applyRequest(property, request);
        return mapper.toResponse(propertyRepository.save(property));
    }

    @Override
    @Transactional
    public PropertyResponse createProperty(Long hostId, PropertyCreateRequest request, List<MultipartFile> images) {
        List<MultipartFile> nonEmptyImages = nonEmptyImages(images);
        validateImageBatch(nonEmptyImages, 0);
        PropertyResponse property = createProperty(hostId, request);
        uploadImages(hostId, property.getId(), nonEmptyImages);
        return property;
    }

    @Override
    @Transactional
    public PropertyResponse updateProperty(Long hostId, Long propertyId, PropertyUpdateRequest request) {
        Property property = lockOwnedProperty(hostId, propertyId);
        if (request.getStatus() == PropertyStatus.ACTIVE
                && imageRepository.findFirstByPropertyIdAndCoverTrue(propertyId).isEmpty()) {
            throw new BusinessException("ERR_PROPERTY_COVER_REQUIRED",
                    "An active property must have a cover image.");
        }
        applyRequest(property, request);
        property.setStatus(request.getStatus());
        return mapper.toResponse(propertyRepository.save(property));
    }

    @Override
    @Transactional
    public void archiveProperty(Long hostId, Long propertyId) {
        Property property = lockOwnedProperty(hostId, propertyId);
        property.setStatus(PropertyStatus.INACTIVE);
        propertyRepository.save(property);
    }

    @Override
    @Transactional
    public PropertyImageResponse uploadImage(Long hostId, Long propertyId, MultipartFile file, boolean cover) {
        Property property = lockOwnedProperty(hostId, propertyId);
        validateImage(file);
        long imageCount = imageRepository.countByPropertyId(propertyId);
        if (imageCount >= storageProperties.getMaxImagesPerProperty()) {
            throw new BusinessException("ERR_IMAGE_LIMIT", "The property image limit has been reached.");
        }

        StoredFile storedFile = storageService.store("properties/" + propertyId, file);
        deleteStorageOnRollback(storedFile.publicId());
        boolean shouldBeCover = cover || imageCount == 0;
        if (shouldBeCover) {
            imageRepository.clearCover(propertyId);
        }
        PropertyImage image = PropertyImage.builder()
                .property(property)
                .imageUrl(storedFile.url())
                .publicId(storedFile.publicId())
                .displayOrder(imageRepository.findMaxDisplayOrder(propertyId) + 1)
                .cover(shouldBeCover)
                .build();
        return mapper.toImageResponse(imageRepository.save(image));
    }

    @Override
    @Transactional
    public List<PropertyImageResponse> uploadImages(Long hostId, Long propertyId, List<MultipartFile> files) {
        List<MultipartFile> nonEmptyFiles = nonEmptyImages(files);
        Property property = lockOwnedProperty(hostId, propertyId);
        long currentCount = imageRepository.countByPropertyId(propertyId);
        validateImageBatch(nonEmptyFiles, currentCount);
        boolean cover = currentCount == 0;
        java.util.ArrayList<PropertyImageResponse> uploaded = new java.util.ArrayList<>();
        for (MultipartFile file : nonEmptyFiles) {
            // The property row lock held by this transaction serializes image order and cover changes.
            uploaded.add(uploadImage(property.getHost().getId(), propertyId, file, cover));
            cover = false;
        }
        return uploaded;
    }

    @Override
    @Transactional
    public void deleteImage(Long hostId, Long propertyId, Long imageId) {
        Property property = lockOwnedProperty(hostId, propertyId);
        PropertyImage image = findPropertyImage(propertyId, imageId);
        String publicId = image.getPublicId();
        boolean wasCover = image.isCover();
        imageRepository.delete(image);
        imageRepository.flush();
        if (wasCover) {
            var nextCover = imageRepository.findFirstByPropertyIdOrderByDisplayOrderAsc(propertyId);
            if (nextCover.isPresent()) {
                nextCover.get().setCover(true);
                imageRepository.save(nextCover.get());
            } else if (property.getStatus() == PropertyStatus.ACTIVE) {
                property.setStatus(PropertyStatus.DRAFT);
                propertyRepository.save(property);
            }
        }
        deleteStorageAfterCommit(publicId);
    }

    @Override
    @Transactional
    public PropertyImageResponse setCoverImage(Long hostId, Long propertyId, Long imageId) {
        lockOwnedProperty(hostId, propertyId);
        PropertyImage image = findPropertyImage(propertyId, imageId);
        if (image.isCover()) {
            return mapper.toImageResponse(image);
        }
        imageRepository.clearCover(propertyId);
        image.setCover(true);
        return mapper.toImageResponse(imageRepository.save(image));
    }

    @Override
    @Transactional
    public List<PropertyImageResponse> reorderImages(Long hostId, Long propertyId,
                                                     PropertyImageOrderRequest request) {
        lockOwnedProperty(hostId, propertyId);
        List<PropertyImage> images = imageRepository.findAllByPropertyIdOrderByDisplayOrderAsc(propertyId);
        List<Long> requestedIds = request.getImageIds();
        if (requestedIds.size() != images.size()
                || new HashSet<>(requestedIds).size() != requestedIds.size()
                || !new HashSet<>(requestedIds).equals(images.stream()
                        .map(PropertyImage::getId)
                        .collect(java.util.stream.Collectors.toSet()))) {
            throw new BusinessException("ERR_IMAGE_ORDER", "The image order must include every property image once.");
        }

        int temporaryOffset = imageRepository.findMaxDisplayOrder(propertyId) + images.size() + 1;
        for (int index = 0; index < requestedIds.size(); index++) {
            imageRepository.updateDisplayOrder(propertyId, requestedIds.get(index), temporaryOffset + index);
        }
        for (int index = 0; index < requestedIds.size(); index++) {
            imageRepository.updateDisplayOrder(propertyId, requestedIds.get(index), index);
        }
        return imageRepository.findAllByPropertyIdOrderByDisplayOrderAsc(propertyId).stream()
                .map(mapper::toImageResponse)
                .toList();
    }

    private Property findOwnedProperty(Long hostId, Long propertyId) {
        return propertyRepository.findByIdAndHostId(propertyId, hostId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    }

    private Property lockOwnedProperty(Long hostId, Long propertyId) {
        return propertyRepository.findOwnedPropertyForUpdate(propertyId, hostId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    }

    private PropertyImage findPropertyImage(Long propertyId, Long imageId) {
        return imageRepository.findByIdAndPropertyId(imageId, propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property image not found"));
    }

    private void applyRequest(Property property, PropertyCreateRequest request) {
        property.setTitle(request.getTitle().trim());
        property.setDescription(request.getDescription().trim());
        property.setAddress(request.getAddress().trim());
        property.setCity(request.getCity().trim());
        property.setPricePerNight(request.getPricePerNight());
        property.setCleaningFee(request.getCleaningFee());
        property.setMaxGuests(request.getMaxGuests());
        property.setBedrooms(request.getBedrooms());
        property.setBeds(request.getBeds());
        property.setBathrooms(request.getBathrooms());
        property.setPropertyType(request.getPropertyType());
        property.setAmenities(resolveAmenities(request.getAmenityIds()));
    }

    private Set<Amenity> resolveAmenities(Set<Long> amenityIds) {
        if (amenityIds == null || amenityIds.isEmpty()) {
            return new LinkedHashSet<>();
        }
        Set<Long> uniqueIds = new LinkedHashSet<>(amenityIds);
        List<Amenity> amenities = amenityRepository.findAllById(uniqueIds);
        if (amenities.size() != uniqueIds.size()) {
            throw new BusinessException("ERR_AMENITY_NOT_FOUND", "One or more amenities do not exist.");
        }
        return new LinkedHashSet<>(amenities);
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("ERR_IMAGE_EMPTY", "An image file is required.");
        }
        if (file.getSize() > storageProperties.getMaxFileSize().toBytes()) {
            throw new BusinessException("ERR_IMAGE_SIZE", "The image exceeds the configured size limit.");
        }
        String contentType = file.getContentType();
        if (!ALLOWED_IMAGE_TYPES.contains(contentType) || !isDecodableImage(file, contentType)) {
            throw new BusinessException("ERR_IMAGE_TYPE", "Only valid JPEG and PNG images are supported.");
        }
    }

    private boolean isDecodableImage(MultipartFile file, String contentType) {
        try (InputStream inputStream = file.getInputStream();
             ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream)) {
            if (imageInputStream == null) {
                return false;
            }
            var readers = ImageIO.getImageReaders(imageInputStream);
            if (!readers.hasNext()) {
                return false;
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(imageInputStream, true, true);
                String expectedFormat = contentType.equals("image/png") ? "PNG" : "JPEG";
                if (!reader.getFormatName().equalsIgnoreCase(expectedFormat)) {
                    return false;
                }
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                if (width <= 0 || height <= 0 || (long) width * height > MAX_IMAGE_PIXELS) {
                    return false;
                }
                BufferedImage decoded = reader.read(0);
                return decoded != null;
            } finally {
                reader.dispose();
            }
        } catch (IOException exception) {
            throw new BusinessException("ERR_IMAGE_READ", "The image could not be read.");
        }
    }

    private List<MultipartFile> nonEmptyImages(List<MultipartFile> files) {
        if (files == null) {
            return List.of();
        }
        return files.stream().filter(file -> file != null && !file.isEmpty()).toList();
    }

    private void validateImageBatch(List<MultipartFile> files, long currentCount) {
        if (currentCount + files.size() > storageProperties.getMaxImagesPerProperty()) {
            throw new BusinessException("ERR_IMAGE_LIMIT", "The property image limit has been reached.");
        }
        files.forEach(this::validateImage);
    }

    private void deleteStorageOnRollback(String publicId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == STATUS_ROLLED_BACK) {
                    safeDeleteStorage(publicId);
                }
            }
        });
    }

    private void deleteStorageAfterCommit(String publicId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                safeDeleteStorage(publicId);
            }
        });
    }

    private void safeDeleteStorage(String publicId) {
        try {
            storageService.delete(publicId);
        } catch (RuntimeException exception) {
            log.error("Unable to clean up property image from storage: {}", publicId, exception);
        }
    }
}
