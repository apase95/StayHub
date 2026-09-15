package com.stayhub.property;

import com.stayhub.common.response.ApiResponse;
import com.stayhub.property.dto.PropertyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyRestController {

    private final PropertyService propertyService;

    @GetMapping("/{id}")
    public ApiResponse<PropertyResponse> getProperty(@PathVariable Long id) {
        return ApiResponse.success(propertyService.getPublicProperty(id), "Property retrieved successfully");
    }
}
