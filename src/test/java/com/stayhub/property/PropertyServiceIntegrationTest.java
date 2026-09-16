package com.stayhub.property;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.stayhub.common.exception.BusinessException;
import com.stayhub.common.exception.ResourceNotFoundException;
import com.stayhub.property.dto.PropertyCreateRequest;
import com.stayhub.property.dto.PropertyImageOrderRequest;
import com.stayhub.property.dto.PropertyResponse;
import com.stayhub.property.dto.PropertyUpdateRequest;
import com.stayhub.storage.StoredFile;
import com.stayhub.storage.StorageService;
import com.stayhub.support.PostgreSqlIntegrationTest;
import com.stayhub.user.User;
import com.stayhub.user.UserRepository;
import com.stayhub.user.UserRole;
import com.stayhub.user.UserStatus;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

class PropertyServiceIntegrationTest extends PostgreSqlIntegrationTest {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyImageRepository imageRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private StorageService storageService;

    private User host;
    private User otherHost;

    @BeforeEach
    void setUp() {
        imageRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
        host = userRepository.save(host("host@example.com"));
        otherHost = userRepository.save(host("other@example.com"));
    }

    @AfterEach
    void cleanUp() {
        imageRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void propertyLifecycleRequiresOwnershipAndCoverBeforePublication() {
        Long amenityId = amenityRepository.findAllByOrderByNameAsc().getFirst().getId();
        PropertyResponse draft = propertyService.createProperty(host.getId(), createRequest(Set.of(amenityId)));

        assertThat(draft.getStatus()).isEqualTo(PropertyStatus.DRAFT);
        assertThat(draft.getAmenities()).hasSize(1);
        assertThatThrownBy(() -> propertyService.getPublicProperty(draft.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
        assertThatThrownBy(() -> propertyService.getHostProperty(otherHost.getId(), draft.getId()))
                .isInstanceOf(ResourceNotFoundException.class);

        PropertyUpdateRequest activeRequest = updateRequest(Set.of(amenityId), PropertyStatus.ACTIVE);
        assertThatThrownBy(() -> propertyService.updateProperty(host.getId(), draft.getId(), activeRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("cover image");

        when(storageService.store(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new StoredFile("/uploads/property.jpg", "properties/1/property.jpg"));
        var image = propertyService.uploadImage(host.getId(), draft.getId(), image("one.png"), false);
        PropertyResponse active = propertyService.updateProperty(host.getId(), draft.getId(), activeRequest);

        assertThat(image.isCover()).isTrue();
        assertThat(active.getStatus()).isEqualTo(PropertyStatus.ACTIVE);
        assertThat(propertyService.getPublicProperty(draft.getId()).getImages()).hasSize(1);

        propertyService.archiveProperty(host.getId(), draft.getId());
        assertThatThrownBy(() -> propertyService.getPublicProperty(draft.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void imagesCanBeReorderedAndCoverCanBeChanged() {
        PropertyResponse draft = propertyService.createProperty(host.getId(), createRequest(Set.of()));
        when(storageService.store(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new StoredFile("/uploads/one.jpg", "one.jpg"))
                .thenReturn(new StoredFile("/uploads/two.jpg", "two.jpg"));

        var first = propertyService.uploadImage(host.getId(), draft.getId(), image("one.png"), false);
        var second = propertyService.uploadImage(host.getId(), draft.getId(), image("two.png"), false);
        propertyService.setCoverImage(host.getId(), draft.getId(), first.getId());
        assertThat(imageRepository.findFirstByPropertyIdAndCoverTrue(draft.getId()))
                .get().extracting(PropertyImage::getId).isEqualTo(first.getId());
        propertyService.setCoverImage(host.getId(), draft.getId(), second.getId());

        PropertyImageOrderRequest orderRequest = new PropertyImageOrderRequest();
        orderRequest.setImageIds(List.of(second.getId(), first.getId()));
        var reordered = propertyService.reorderImages(host.getId(), draft.getId(), orderRequest);

        assertThat(reordered).extracting(image -> image.getId())
                .containsExactly(second.getId(), first.getId());
        assertThat(imageRepository.findFirstByPropertyIdAndCoverTrue(draft.getId()))
                .get().extracting(PropertyImage::getId).isEqualTo(second.getId());
    }

    @Test
    void propertyWithMultipleAmenitiesDoesNotDuplicateImages() {
        Set<Long> amenityIds = amenityRepository.findAllByOrderByNameAsc().stream()
                .limit(3)
                .map(Amenity::getId)
                .collect(java.util.stream.Collectors.toSet());
        PropertyResponse draft = propertyService.createProperty(host.getId(), createRequest(amenityIds));
        when(storageService.store(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new StoredFile("/uploads/one.jpg", "one.jpg"))
                .thenReturn(new StoredFile("/uploads/two.jpg", "two.jpg"))
                .thenReturn(new StoredFile("/uploads/three.jpg", "three.jpg"));

        var first = propertyService.uploadImage(host.getId(), draft.getId(), image("one.png"), false);
        var second = propertyService.uploadImage(host.getId(), draft.getId(), image("two.png"), false);
        var third = propertyService.uploadImage(host.getId(), draft.getId(), image("three.png"), false);

        PropertyResponse response = propertyService.getHostProperty(host.getId(), draft.getId());

        assertThat(response.getAmenities()).hasSize(3);
        assertThat(response.getImages())
                .extracting(image -> image.getId())
                .containsExactly(first.getId(), second.getId(), third.getId());
    }

    @Test
    void rejectsUnknownAmenitiesAndInvalidImageContent() {
        assertThat(amenityRepository.count()).isGreaterThanOrEqualTo(8);
        assertThatThrownBy(() -> propertyService.createProperty(host.getId(), createRequest(Set.of(Long.MAX_VALUE))))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("amenities");

        PropertyResponse draft = propertyService.createProperty(host.getId(), createRequest(Set.of()));
        MockMultipartFile fakeImage = new MockMultipartFile(
                "file", "fake.jpg", "image/jpeg", "not-an-image".getBytes());
        assertThatThrownBy(() -> propertyService.uploadImage(host.getId(), draft.getId(), fakeImage, false))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("JPEG");
    }

    @Test
    void deletingOnlyCoverReturnsActivePropertyToDraft() {
        PropertyResponse draft = propertyService.createProperty(host.getId(), createRequest(Set.of()));
        when(storageService.store(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new StoredFile("/uploads/only.png", "only.png"));
        var image = propertyService.uploadImage(host.getId(), draft.getId(), image("only.png"), false);
        propertyService.updateProperty(host.getId(), draft.getId(), updateRequest(Set.of(), PropertyStatus.ACTIVE));

        propertyService.deleteImage(host.getId(), draft.getId(), image.getId());

        assertThat(propertyService.getHostProperty(host.getId(), draft.getId()).getStatus())
                .isEqualTo(PropertyStatus.DRAFT);
        org.mockito.Mockito.verify(storageService).delete("only.png");
    }

    private User host(String email) {
        return User.builder()
                .email(email)
                .passwordHash("password-hash")
                .fullName("Test Host")
                .role(UserRole.HOST)
                .status(UserStatus.ACTIVE)
                .build();
    }

    private PropertyCreateRequest createRequest(Set<Long> amenityIds) {
        PropertyCreateRequest request = new PropertyCreateRequest();
        populateRequest(request, amenityIds);
        return request;
    }

    private PropertyUpdateRequest updateRequest(Set<Long> amenityIds, PropertyStatus status) {
        PropertyUpdateRequest request = new PropertyUpdateRequest();
        populateRequest(request, amenityIds);
        request.setStatus(status);
        return request;
    }

    private void populateRequest(PropertyCreateRequest request, Set<Long> amenityIds) {
        request.setTitle("Riverside Villa");
        request.setDescription("A quiet property near the river.");
        request.setAddress("12 River Road");
        request.setCity("Da Nang");
        request.setPricePerNight(new BigDecimal("1500000.00"));
        request.setCleaningFee(new BigDecimal("100000.00"));
        request.setMaxGuests(4);
        request.setBedrooms(2);
        request.setBeds(2);
        request.setBathrooms(2);
        request.setPropertyType(PropertyType.VILLA);
        request.setAmenityIds(amenityIds);
    }

    private MockMultipartFile image(String name) {
        byte[] onePixelPng = Base64.getDecoder().decode(
                "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=");
        return new MockMultipartFile("file", name, "image/png", onePixelPng);
    }
}
