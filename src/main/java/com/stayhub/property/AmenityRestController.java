package com.stayhub.property;

import com.stayhub.common.response.ApiResponse;
import com.stayhub.property.dto.AmenityResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/amenities")
@RequiredArgsConstructor
public class AmenityRestController {

    private final PropertyService propertyService;

    @GetMapping
    public ApiResponse<List<AmenityResponse>> getAmenities() {
        return ApiResponse.success(propertyService.getAmenities(), "Amenities retrieved successfully");
    }
}
