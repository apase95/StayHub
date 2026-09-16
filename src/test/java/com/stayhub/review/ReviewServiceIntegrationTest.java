package com.stayhub.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.stayhub.booking.BookingRepository;
import com.stayhub.booking.BookingService;
import com.stayhub.booking.BookingStatus;
import com.stayhub.booking.dto.BookingCreateRequest;
import com.stayhub.common.exception.BusinessException;
import com.stayhub.payment.PaymentRepository;
import com.stayhub.property.Property;
import com.stayhub.property.PropertyRepository;
import com.stayhub.property.PropertyStatus;
import com.stayhub.property.PropertyType;
import com.stayhub.review.dto.ReviewCreateRequest;
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

class ReviewServiceIntegrationTest extends PostgreSqlIntegrationTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

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
        reviewRepository.deleteAll();
        paymentRepository.deleteAll();
        bookingRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
        host = userRepository.save(user("review-host@example.com", UserRole.HOST));
        guest = userRepository.save(user("review-guest@example.com", UserRole.GUEST));
        property = propertyRepository.save(property(host));
    }

    @AfterEach
    void cleanUp() {
        reviewRepository.deleteAll();
        paymentRepository.deleteAll();
        bookingRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createsReviewForCompletedBookingAndUpdatesPropertyRating() {
        var booking = bookingService.createBooking(guest.getId(), bookingRequest(10, 12));
        bookingService.acceptBooking(host.getId(), booking.getId());
        var completed = bookingService.completeBooking(host.getId(), booking.getId());
        assertThat(completed.getStatus()).isEqualTo(BookingStatus.COMPLETED);

        var review = reviewService.createReview(guest.getId(), booking.getId(), reviewRequest((short) 5, "Excellent stay."));

        assertThat(review.getRating()).isEqualTo((short) 5);
        assertThat(review.getComment()).isEqualTo("Excellent stay.");
        assertThat(propertyRepository.findById(property.getId()).orElseThrow().getRatingAvg())
                .isEqualByComparingTo("5.00");
        assertThat(reviewService.getPropertyReviews(property.getId())).hasSize(1);
    }

    @Test
    void rejectsReviewBeforeCompletionAndDuplicateReviews() {
        var booking = bookingService.createBooking(guest.getId(), bookingRequest(20, 22));

        assertThatThrownBy(() -> reviewService.createReview(guest.getId(), booking.getId(), reviewRequest((short) 4, "Too early.")))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo("ERR_REVIEW_NOT_ALLOWED");

        bookingService.acceptBooking(host.getId(), booking.getId());
        bookingService.completeBooking(host.getId(), booking.getId());
        reviewService.createReview(guest.getId(), booking.getId(), reviewRequest((short) 4, "Good stay."));

        assertThatThrownBy(() -> reviewService.createReview(guest.getId(), booking.getId(), reviewRequest((short) 5, "Again.")))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo("ERR_REVIEW_EXISTS");
    }

    private BookingCreateRequest bookingRequest(int checkInOffset, int checkOutOffset) {
        BookingCreateRequest request = new BookingCreateRequest();
        request.setPropertyId(property.getId());
        request.setCheckInDate(LocalDate.now().plusDays(checkInOffset));
        request.setCheckOutDate(LocalDate.now().plusDays(checkOutOffset));
        request.setGuests(2);
        return request;
    }

    private ReviewCreateRequest reviewRequest(short rating, String comment) {
        ReviewCreateRequest request = new ReviewCreateRequest();
        request.setRating(rating);
        request.setComment(comment);
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
                .title("Review House")
                .description("A stay ready for reviews.")
                .address("9 Review Road")
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
