package com.stayhub.payment.dto;

import com.stayhub.booking.BookingStatus;
import com.stayhub.payment.PaymentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentStatusResponse {
    private Long bookingId;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private String providerTransactionNo;
}
