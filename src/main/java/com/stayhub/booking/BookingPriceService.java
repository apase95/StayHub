package com.stayhub.booking;

import com.stayhub.booking.dto.BookingPriceQuote;
import com.stayhub.property.Property;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class BookingPriceService {

    private static final BigDecimal SERVICE_FEE_RATE = new BigDecimal("0.10");

    public BookingPriceQuote calculate(Property property, LocalDate checkInDate, LocalDate checkOutDate) {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal subtotal = property.getPricePerNight()
                .multiply(BigDecimal.valueOf(nights))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal serviceFee = subtotal.multiply(SERVICE_FEE_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalPrice = subtotal.add(property.getCleaningFee()).add(serviceFee).setScale(2, RoundingMode.HALF_UP);

        return BookingPriceQuote.builder()
                .nights(nights)
                .nightlyPrice(property.getPricePerNight())
                .subtotal(subtotal)
                .cleaningFee(property.getCleaningFee())
                .serviceFee(serviceFee)
                .totalPrice(totalPrice)
                .build();
    }
}
