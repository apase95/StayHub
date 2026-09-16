package com.stayhub.booking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.stayhub.booking.dto.AvailabilityRequest;
import com.stayhub.booking.dto.BookingCreateRequest;
import com.stayhub.common.exception.BusinessException;
import com.stayhub.common.exception.InvalidStateTransitionException;
import com.stayhub.payment.PaymentMethod;
import com.stayhub.payment.PaymentRepository;
import com.stayhub.payment.PaymentStatus;
import com.stayhub.property.Property;
import com.stayhub.property.PropertyRepository;
import com.stayhub.property.PropertyStatus;
import com.stayhub.property.PropertyType;
import com.stayhub.support.PostgreSqlIntegrationTest;
import com.stayhub.user.User;
import com.stayhub.user.UserRepository;
import com.stayhub.user.UserRole;
import com.stayhub.user.UserStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BookingServiceIntegrationTest extends PostgreSqlIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    private User host;
    private User guest;
    private Property property;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        bookingRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
        host = userRepository.save(user("booking-host@example.com", UserRole.HOST));
        guest = userRepository.save(user("booking-guest@example.com", UserRole.GUEST));
        property = propertyRepository.save(property(host));
    }

    @AfterEach
    void cleanUp() {
        paymentRepository.deleteAll();
        bookingRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createsPendingBookingWithPriceSnapshot() {
        BookingCreateRequest request = bookingRequest(
                LocalDate.now().plusDays(10), LocalDate.now().plusDays(13), 2);

        var response = bookingService.createBooking(guest.getId(), request);

        assertThat(response.getStatus()).isEqualTo(BookingStatus.PENDING);
        assertThat(response.getPaymentMethod()).isEqualTo(PaymentMethod.MOCK);
        assertThat(response.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(response.getTransactionId()).startsWith("MOCK-");
        assertThat(response.getNightlyPrice()).isEqualByComparingTo("1000000.00");
        assertThat(response.getSubtotal()).isEqualByComparingTo("3000000.00");
        assertThat(response.getCleaningFee()).isEqualByComparingTo("100000.00");
        assertThat(response.getServiceFee()).isEqualByComparingTo("300000.00");
        assertThat(response.getTotalPrice()).isEqualByComparingTo("3400000.00");

        property.setPricePerNight(new BigDecimal("2000000.00"));
        propertyRepository.save(property);

        Booking saved = bookingRepository.findById(response.getId()).orElseThrow();
        assertThat(saved.getNightlyPrice()).isEqualByComparingTo("1000000.00");
        assertThat(paymentRepository.findByBookingId(response.getId())).isPresent();
    }

    @Test
    void hostCanAcceptOrRejectPendingBookingsOnly() {
        var booking = bookingService.createBooking(guest.getId(), bookingRequest(
                LocalDate.now().plusDays(40), LocalDate.now().plusDays(42), 2));

        var accepted = bookingService.acceptBooking(host.getId(), booking.getId());

        assertThat(accepted.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThatThrownBy(() -> bookingService.rejectBooking(host.getId(), booking.getId()))
                .isInstanceOf(InvalidStateTransitionException.class);
    }

    @Test
    void guestCanCancelPendingOrConfirmedBooking() {
        var booking = bookingService.createBooking(guest.getId(), bookingRequest(
                LocalDate.now().plusDays(50), LocalDate.now().plusDays(52), 2));

        var cancelled = bookingService.cancelBooking(guest.getId(), booking.getId());

        assertThat(cancelled.getStatus()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(cancelled.getCancelledAt()).isNotNull();
        assertThatThrownBy(() -> bookingService.cancelBooking(guest.getId(), booking.getId()))
                .isInstanceOf(InvalidStateTransitionException.class);
    }

    @Test
    void rejectsOverlappingBlockingBookings() {
        bookingService.createBooking(guest.getId(), bookingRequest(
                LocalDate.now().plusDays(20), LocalDate.now().plusDays(23), 2));

        BookingCreateRequest overlapping = bookingRequest(
                LocalDate.now().plusDays(22), LocalDate.now().plusDays(24), 2);

        assertThatThrownBy(() -> bookingService.createBooking(guest.getId(), overlapping))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo("ERR_ROOM_NOT_AVAILABLE");
    }

    @Test
    void availabilityReturnsQuoteWhenDatesAreFree() {
        AvailabilityRequest request = new AvailabilityRequest();
        request.setPropertyId(property.getId());
        request.setCheckInDate(LocalDate.now().plusDays(30));
        request.setCheckOutDate(LocalDate.now().plusDays(32));
        request.setGuests(2);

        var response = bookingService.checkAvailability(request);

        assertThat(response.isAvailable()).isTrue();
        assertThat(response.getPriceQuote().getTotalPrice()).isEqualByComparingTo("2300000.00");
    }

    private BookingCreateRequest bookingRequest(LocalDate checkInDate, LocalDate checkOutDate, int guests) {
        BookingCreateRequest request = new BookingCreateRequest();
        request.setPropertyId(property.getId());
        request.setCheckInDate(checkInDate);
        request.setCheckOutDate(checkOutDate);
        request.setGuests(guests);
        return request;
    }

    private User user(String email, UserRole role) {
        return User.builder()
                .email(email)
                .passwordHash("password-hash")
                .fullName(role.name() + " User")
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();
    }

    private Property property(User host) {
        return Property.builder()
                .host(host)
                .title("Beach House")
                .description("A bright stay near the beach.")
                .address("1 Ocean Street")
                .city("Da Nang")
                .pricePerNight(new BigDecimal("1000000.00"))
                .cleaningFee(new BigDecimal("100000.00"))
                .maxGuests(4)
                .bedrooms(2)
                .beds(2)
                .bathrooms(2)
                .propertyType(PropertyType.HOUSE)
                .status(PropertyStatus.ACTIVE)
                .ratingAvg(BigDecimal.ZERO)
                .build();
    }
}
