package com.stayhub.payment;

import com.stayhub.booking.Booking;

public interface PaymentService {
    Payment createSuccessfulPayment(Booking booking);
}
