package com.stayhub.property;

import com.stayhub.property.dto.AmenityResponse;
import com.stayhub.property.dto.PropertyCreateRequest;
import com.stayhub.property.dto.PropertyImageResponse;
import com.stayhub.property.dto.PropertyResponse;
import com.stayhub.property.dto.PropertySummary;
import com.stayhub.property.dto.PropertyUpdateRequest;
import java.util.Comparator;
import org.springframework.stereotype.Component;

@Component
public class PropertyMapper {

    public PropertyResponse toResponse(Property property) {
        return PropertyResponse.builder()
                .id(property.getId())
                .hostId(property.getHost().getId())
                .hostName(property.getHost().getFullName())
                .title(property.getTitle())
                .description(property.getDescription())
                .address(property.getAddress())
                .city(property.getCity())
                .pricePerNight(property.getPricePerNight())
                .cleaningFee(property.getCleaningFee())
                .maxGuests(property.getMaxGuests())
                .bedrooms(property.getBedrooms())
                .beds(property.getBeds())
                .bathrooms(property.getBathrooms())
                .propertyType(property.getPropertyType())
                .status(property.getStatus())
                .ratingAvg(property.getRatingAvg())
                .images(property.getImages().stream()
                        .sorted(Comparator.comparing(PropertyImage::getDisplayOrder))
                        .map(this::toImageResponse)
                        .toList())
                .amenities(property.getAmenities().stream()
                        .sorted(Comparator.comparing(Amenity::getName))
                        .map(this::toAmenityResponse)
                        .toList())
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())
                .build();
    }

    public PropertySummary toSummary(Property property) {
        String coverImageUrl = property.getImages().stream()
                .filter(PropertyImage::isCover)
                .findFirst()
                .or(() -> property.getImages().stream().findFirst())
                .map(PropertyImage::getImageUrl)
                .orElse(null);
        return PropertySummary.builder()
                .id(property.getId())
                .title(property.getTitle())
                .city(property.getCity())
                .pricePerNight(property.getPricePerNight())
                .ratingAvg(property.getRatingAvg())
                .propertyType(property.getPropertyType())
                .status(property.getStatus())
                .coverImageUrl(coverImageUrl)
                .build();
    }

    public PropertyImageResponse toImageResponse(PropertyImage image) {
        return PropertyImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .displayOrder(image.getDisplayOrder())
                .cover(image.isCover())
                .build();
    }

    public AmenityResponse toAmenityResponse(Amenity amenity) {
        return AmenityResponse.builder()
                .id(amenity.getId())
                .name(amenity.getName())
                .icon(amenity.getIcon())
                .build();
    }

    public PropertyUpdateRequest toUpdateRequest(Property property) {
        PropertyUpdateRequest request = new PropertyUpdateRequest();
        copyFields(property, request);
        request.setStatus(property.getStatus());
        request.setAmenityIds(property.getAmenities().stream().map(Amenity::getId).collect(java.util.stream.Collectors.toSet()));
        return request;
    }

    public void copyFields(Property property, PropertyCreateRequest request) {
        request.setTitle(property.getTitle());
        request.setDescription(property.getDescription());
        request.setAddress(property.getAddress());
        request.setCity(property.getCity());
        request.setPricePerNight(property.getPricePerNight());
        request.setCleaningFee(property.getCleaningFee());
        request.setMaxGuests(property.getMaxGuests());
        request.setBedrooms(property.getBedrooms());
        request.setBeds(property.getBeds());
        request.setBathrooms(property.getBathrooms());
        request.setPropertyType(property.getPropertyType());
    }
}
