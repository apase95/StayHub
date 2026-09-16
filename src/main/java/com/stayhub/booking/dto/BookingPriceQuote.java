package com.stayhub.booking.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingPriceQuote {
    private long nights;
    private BigDecimal nightlyPrice;
    private BigDecimal subtotal;
    private BigDecimal cleaningFee;
    private BigDecimal serviceFee;
    private BigDecimal totalPrice;
}
