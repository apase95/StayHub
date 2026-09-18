package com.stayhub.payment;

import java.math.BigDecimal;

public record VnpayPaymentRequest(
        String txnRef,
        BigDecimal amount,
        String orderInfo,
        String ipAddress
) {
}
