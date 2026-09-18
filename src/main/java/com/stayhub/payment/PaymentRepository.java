package com.stayhub.payment;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);

    @Query("""
            select p from Payment p
            join fetch p.booking b
            join fetch b.guest
            where p.providerTxnRef = :providerTxnRef
            """)
    Optional<Payment> findByProviderTxnRefWithBooking(@Param("providerTxnRef") String providerTxnRef);
}
