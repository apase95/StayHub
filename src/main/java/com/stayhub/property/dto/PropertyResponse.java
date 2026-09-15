package com.stayhub.property.dto;

import com.stayhub.property.PropertyStatus;
import com.stayhub.property.PropertyType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    private Long id;
    private Long hostId;
    private String hostName;
    private String title;
    private String description;
    private String address;
    private String city;
    private BigDecimal pricePerNight;
    private BigDecimal cleaningFee;
    private Integer maxGuests;
    private Integer bedrooms;
    private Integer beds;
    private Integer bathrooms;
    private PropertyType propertyType;
    private PropertyStatus status;
    private BigDecimal ratingAvg;
    @Builder.Default
    private List<PropertyImageResponse> images = new ArrayList<>();
    @Builder.Default
    private List<AmenityResponse> amenities = new ArrayList<>();
    private Instant createdAt;
    private Instant updatedAt;
}
