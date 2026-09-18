package com.stayhub.payment;

import com.stayhub.booking.Booking;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MockPaymentService implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment createSuccessfulPayment(Booking booking) {
        Payment payment = Payment.builder()
                .booking(booking)
                .paymentMethod(PaymentMethod.MOCK)
                .status(PaymentStatus.SUCCESS)
                .amount(booking.getTotalPrice())
                .provider(PaymentMethod.MOCK.name())
                .currency("VND")
                .transactionId("MOCK-" + UUID.randomUUID())
                .paidAt(Instant.now())
                .build();
        return paymentRepository.save(payment);
    }
}
