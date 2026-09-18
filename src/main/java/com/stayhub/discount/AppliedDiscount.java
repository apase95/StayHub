package com.stayhub.discount;

import java.math.BigDecimal;

public record AppliedDiscount(Long discountCodeId, String code, BigDecimal amount) {
    public static AppliedDiscount none() {
        return new AppliedDiscount(null, null, BigDecimal.ZERO);
    }
}
