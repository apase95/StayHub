package com.stayhub.host;

import com.stayhub.auth.UserPrincipal;
import com.stayhub.common.response.ApiResponse;
import com.stayhub.property.PropertyService;
import com.stayhub.property.dto.PropertyCreateRequest;
import com.stayhub.property.dto.PropertyImageOrderRequest;
import com.stayhub.property.dto.PropertyImageResponse;
import com.stayhub.property.dto.PropertyResponse;
import com.stayhub.property.dto.PropertySummary;
import com.stayhub.property.dto.PropertyUpdateRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api/v1/host/properties")
@RequiredArgsConstructor
public class HostPropertyRestController {

    private final PropertyService propertyService;

    @GetMapping
    public ApiResponse<List<PropertySummary>> getProperties(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(propertyService.getHostProperties(principal.getId()),
                "Host properties retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<PropertyResponse> getProperty(@AuthenticationPrincipal UserPrincipal principal,
                                                     @PathVariable Long id) {
        return ApiResponse.success(propertyService.getHostProperty(principal.getId(), id),
                "Host property retrieved successfully");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PropertyResponse> createProperty(@AuthenticationPrincipal UserPrincipal principal,
                                                        @Valid @RequestBody PropertyCreateRequest request) {
        return ApiResponse.success(propertyService.createProperty(principal.getId(), request),
                "Property created as a draft");
    }

    @PutMapping("/{id}")
    public ApiResponse<PropertyResponse> updateProperty(@AuthenticationPrincipal UserPrincipal principal,
                                                        @PathVariable Long id,
                                                        @Valid @RequestBody PropertyUpdateRequest request) {
        return ApiResponse.success(propertyService.updateProperty(principal.getId(), id, request),
                "Property updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> archiveProperty(@AuthenticationPrincipal UserPrincipal principal,
                                             @PathVariable Long id) {
        propertyService.archiveProperty(principal.getId(), id);
        return ApiResponse.success(null, "Property archived successfully");
    }

    @PostMapping("/{propertyId}/images")
    public ApiResponse<PropertyImageResponse> uploadImage(@AuthenticationPrincipal UserPrincipal principal,
                                                          @PathVariable Long propertyId,
                                                          @RequestParam MultipartFile file,
                                                          @RequestParam(defaultValue = "false") boolean cover) {
        return ApiResponse.success(propertyService.uploadImage(principal.getId(), propertyId, file, cover),
                "Property image uploaded successfully");
    }

    @DeleteMapping("/{propertyId}/images/{imageId}")
    public ApiResponse<Void> deleteImage(@AuthenticationPrincipal UserPrincipal principal,
                                         @PathVariable Long propertyId,
                                         @PathVariable Long imageId) {
        propertyService.deleteImage(principal.getId(), propertyId, imageId);
        return ApiResponse.success(null, "Property image deleted successfully");
    }

    @PutMapping("/{propertyId}/images/{imageId}/cover")
    public ApiResponse<PropertyImageResponse> setCoverImage(@AuthenticationPrincipal UserPrincipal principal,
                                                            @PathVariable Long propertyId,
                                                            @PathVariable Long imageId) {
        return ApiResponse.success(propertyService.setCoverImage(principal.getId(), propertyId, imageId),
                "Cover image updated successfully");
    }

    @PutMapping("/{propertyId}/images/order")
    public ApiResponse<List<PropertyImageResponse>> reorderImages(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long propertyId,
            @Valid @RequestBody PropertyImageOrderRequest request) {
        return ApiResponse.success(propertyService.reorderImages(principal.getId(), propertyId, request),
                "Property images reordered successfully");
    }
}
