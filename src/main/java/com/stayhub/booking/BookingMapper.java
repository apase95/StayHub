package com.stayhub.booking;

import com.stayhub.booking.dto.BookingResponse;
import com.stayhub.property.PropertyImage;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking) {
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        String coverImageUrl = booking.getProperty().getImages().stream()
                .filter(PropertyImage::isCover)
                .findFirst()
                .or(() -> booking.getProperty().getImages().stream().findFirst())
                .map(PropertyImage::getImageUrl)
                .orElse(null);
        return BookingResponse.builder()
                .id(booking.getId())
                .propertyId(booking.getProperty().getId())
                .propertyTitle(booking.getProperty().getTitle())
                .propertyCity(booking.getProperty().getCity())
                .propertyImageUrl(coverImageUrl)
                .hostId(booking.getProperty().getHost().getId())
                .hostName(booking.getProperty().getHost().getFullName())
                .guestId(booking.getGuest().getId())
                .guestName(booking.getGuest().getFullName())
                .guestEmail(booking.getGuest().getEmail())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .guests(booking.getGuests())
                .nights(nights)
                .nightlyPrice(booking.getNightlyPrice())
                .subtotal(booking.getNightlyPrice().multiply(BigDecimal.valueOf(nights)))
                .cleaningFee(booking.getCleaningFee())
                .serviceFee(booking.getServiceFee())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .cancelledAt(booking.getCancelledAt())
                .build();
    }
}
