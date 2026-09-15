package com.stayhub.property;

import com.stayhub.property.dto.AmenityResponse;
import com.stayhub.property.dto.PropertyCreateRequest;
import com.stayhub.property.dto.PropertyImageOrderRequest;
import com.stayhub.property.dto.PropertyImageResponse;
import com.stayhub.property.dto.PropertyResponse;
import com.stayhub.property.dto.PropertySummary;
import com.stayhub.property.dto.PropertyUpdateRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PropertyService {
    PropertyResponse getPublicProperty(Long propertyId);

    PropertyResponse getHostProperty(Long hostId, Long propertyId);

    PropertyUpdateRequest getHostPropertyForEdit(Long hostId, Long propertyId);

    List<PropertySummary> getHostProperties(Long hostId);

    List<AmenityResponse> getAmenities();

    PropertyResponse createProperty(Long hostId, PropertyCreateRequest request);

    PropertyResponse createProperty(Long hostId, PropertyCreateRequest request, List<MultipartFile> images);

    PropertyResponse updateProperty(Long hostId, Long propertyId, PropertyUpdateRequest request);

    void archiveProperty(Long hostId, Long propertyId);

    PropertyImageResponse uploadImage(Long hostId, Long propertyId, MultipartFile file, boolean cover);

    List<PropertyImageResponse> uploadImages(Long hostId, Long propertyId, List<MultipartFile> files);

    void deleteImage(Long hostId, Long propertyId, Long imageId);

    PropertyImageResponse setCoverImage(Long hostId, Long propertyId, Long imageId);

    List<PropertyImageResponse> reorderImages(Long hostId, Long propertyId, PropertyImageOrderRequest request);
}
