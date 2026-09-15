package com.stayhub.property.dto;

import com.stayhub.property.PropertyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;

@Data
public class PropertyCreateRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    @Size(max = 500)
    private String address;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal pricePerNight;

    @NotNull
    @DecimalMin(value = "0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal cleaningFee = BigDecimal.ZERO;

    @NotNull
    @Min(1)
    private Integer maxGuests;

    @NotNull
    @Min(0)
    private Integer bedrooms;

    @NotNull
    @Min(0)
    private Integer beds;

    @NotNull
    @Min(0)
    private Integer bathrooms;

    @NotNull
    private PropertyType propertyType;

    private Set<Long> amenityIds = new LinkedHashSet<>();
}
