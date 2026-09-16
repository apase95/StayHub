package com.stayhub.booking;

import com.stayhub.booking.dto.AvailabilityRequest;
import com.stayhub.booking.dto.AvailabilityResponse;
import com.stayhub.booking.dto.BookingCreateRequest;
import com.stayhub.booking.dto.BookingPriceQuote;
import com.stayhub.booking.dto.BookingResponse;
import com.stayhub.common.exception.BusinessException;
import com.stayhub.common.exception.ResourceNotFoundException;
import com.stayhub.property.Property;
import com.stayhub.property.PropertyRepository;
import com.stayhub.property.PropertyStatus;
import com.stayhub.user.User;
import com.stayhub.user.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private static final Set<BookingStatus> BLOCKING_STATUSES = Set.of(BookingStatus.PENDING, BookingStatus.CONFIRMED);

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final BookingPriceService bookingPriceService;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse createBooking(Long guestId, BookingCreateRequest request) {
        validateDates(request.getCheckInDate(), request.getCheckOutDate());
        User guest = userRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));
        Property property = propertyRepository.findByIdAndStatusForUpdate(request.getPropertyId(), PropertyStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        validateGuests(property, request.getGuests());
        ensureAvailable(property.getId(), request.getCheckInDate(), request.getCheckOutDate());

        BookingPriceQuote quote = bookingPriceService.calculate(property, request.getCheckInDate(), request.getCheckOutDate());
        Booking booking = Booking.builder()
                .property(property)
                .guest(guest)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .guests(request.getGuests())
                .nightlyPrice(quote.getNightlyPrice())
                .cleaningFee(quote.getCleaningFee())
                .serviceFee(quote.getServiceFee())
                .totalPrice(quote.getTotalPrice())
                .status(BookingStatus.PENDING)
                .build();

        try {
            return bookingMapper.toResponse(bookingRepository.saveAndFlush(booking));
        } catch (DataIntegrityViolationException exception) {
            throw roomNotAvailable();
        }
    }

    @Override
    public AvailabilityResponse checkAvailability(AvailabilityRequest request) {
        BookingPriceQuote quote = quote(request.getPropertyId(), request.getCheckInDate(), request.getCheckOutDate(), request.getGuests());
        ensureAvailable(request.getPropertyId(), request.getCheckInDate(), request.getCheckOutDate());
        return AvailabilityResponse.builder()
                .available(true)
                .priceQuote(quote)
                .build();
    }

    @Override
    public BookingPriceQuote quote(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate, Integer guests) {
        validateDates(checkInDate, checkOutDate);
        Property property = propertyRepository.findByIdAndStatus(propertyId, PropertyStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        validateGuests(property, guests);
        return bookingPriceService.calculate(property, checkInDate, checkOutDate);
    }

    @Override
    public BookingResponse getBookingForGuest(Long guestId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .filter(found -> found.getGuest().getId().equals(guestId))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return bookingMapper.toResponse(booking);
    }

    @Override
    public List<BookingResponse> getGuestBookings(Long guestId) {
        return bookingRepository.findByGuestId(guestId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> getBookingRequestsByHost(Long hostId) {
        return bookingRepository.findBookingRequestsByHost(hostId, BookingStatus.PENDING).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    private void ensureAvailable(Long propertyId, LocalDate checkInDate, LocalDate checkOutDate) {
        if (!bookingRepository.findConflictingBookings(propertyId, checkInDate, checkOutDate, BLOCKING_STATUSES).isEmpty()) {
            throw roomNotAvailable();
        }
    }

    private void validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null || !checkInDate.isBefore(checkOutDate)) {
            throw new BusinessException("ERR_INVALID_DATE_RANGE", "Check-out date must be after check-in date.");
        }
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new BusinessException("ERR_INVALID_DATE_RANGE", "Check-in date cannot be in the past.");
        }
    }

    private void validateGuests(Property property, Integer guests) {
        if (guests == null || guests < 1) {
            throw new BusinessException("ERR_INVALID_GUESTS", "Guest count must be at least 1.");
        }
        if (guests > property.getMaxGuests()) {
            throw new BusinessException("ERR_TOO_MANY_GUESTS", "Guest count exceeds the property capacity.");
        }
    }

    private BusinessException roomNotAvailable() {
        return new BusinessException("ERR_ROOM_NOT_AVAILABLE", "The selected dates are no longer available.");
    }
}
