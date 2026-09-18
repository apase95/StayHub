package com.stayhub.discount;

import com.stayhub.auth.UserPrincipal;
import com.stayhub.common.response.ApiResponse;
import com.stayhub.discount.dto.DiscountValidateRequest;
import com.stayhub.discount.dto.DiscountValidationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/discounts")
@RequiredArgsConstructor
public class DiscountRestController {
    private final DiscountService discountService;

    @PostMapping("/validate")
    public ApiResponse<DiscountValidationResponse> validate(@AuthenticationPrincipal UserPrincipal principal,
                                                            @Valid @RequestBody DiscountValidateRequest request) {
        return ApiResponse.success(discountService.validate(principal.getId(), request), "Discount code applied.");
    }
}
