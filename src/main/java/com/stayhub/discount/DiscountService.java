package com.stayhub.discount;

import com.stayhub.discount.dto.DiscountValidateRequest;
import com.stayhub.discount.dto.DiscountValidationResponse;
import java.math.BigDecimal;

public interface DiscountService {
    DiscountValidationResponse validate(Long guestId, DiscountValidateRequest request);

    AppliedDiscount apply(Long guestId, String code, BigDecimal subtotalPrice);
}
