package com.stayhub.common.util;

import java.math.BigDecimal;

public class PriceUtil {

    private PriceUtil() {}

    // Formula: (nightlyPrice * nights) + cleaningFee + serviceFee
    public static BigDecimal calculateTotalPrice(BigDecimal nightlyPrice, long nights, BigDecimal cleaningFee, BigDecimal serviceFee) {
        if (nightlyPrice == null || nightlyPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Nightly price cannot be null or negative.");
        }
        
        BigDecimal totalStayCost = nightlyPrice.multiply(BigDecimal.valueOf(nights));
        BigDecimal total = totalStayCost;
        
        if (cleaningFee != null) {
            total = total.add(cleaningFee);
        }
        if (serviceFee != null) {
            total = total.add(serviceFee);
        }
        
        return total;
    }
}