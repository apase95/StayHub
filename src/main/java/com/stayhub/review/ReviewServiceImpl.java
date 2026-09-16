package com.stayhub.review;

import com.stayhub.booking.Booking;
import com.stayhub.booking.BookingRepository;
import com.stayhub.booking.BookingStatus;
import com.stayhub.common.exception.BusinessException;
import com.stayhub.common.exception.ResourceNotFoundException;
import com.stayhub.property.Property;
import com.stayhub.property.PropertyRepository;
import com.stayhub.review.dto.ReviewCreateRequest;
import com.stayhub.review.dto.ReviewResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse createReview(Long guestId, Long bookingId, ReviewCreateRequest request) {
        Booking booking = bookingRepository.findDetailedById(bookingId)
                .filter(found -> found.getGuest().getId().equals(guestId))
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BusinessException("ERR_REVIEW_NOT_ALLOWED", "Only completed bookings can be reviewed.");
        }
        if (reviewRepository.existsByBookingId(bookingId)) {
            throw new BusinessException("ERR_REVIEW_EXISTS", "This booking has already been reviewed.");
        }

        Property property = propertyRepository.findById(booking.getProperty().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        Review review = Review.builder()
                .booking(booking)
                .property(property)
                .guest(booking.getGuest())
                .rating(request.getRating())
                .comment(normalizeComment(request.getComment()))
                .build();

        try {
            Review saved = reviewRepository.saveAndFlush(review);
            updatePropertyRating(property.getId());
            return reviewMapper.toResponse(saved);
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException("ERR_REVIEW_EXISTS", "This booking has already been reviewed.");
        }
    }

    @Override
    public List<ReviewResponse> getPropertyReviews(Long propertyId) {
        return reviewRepository.findByPropertyIdOrderByCreatedAtDesc(propertyId).stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<ReviewResponse> getReviewByBooking(Long bookingId) {
        return reviewRepository.findByBookingId(bookingId).map(reviewMapper::toResponse);
    }

    @Override
    public boolean hasReview(Long bookingId) {
        return reviewRepository.existsByBookingId(bookingId);
    }

    private void updatePropertyRating(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        BigDecimal average = BigDecimal.valueOf(reviewRepository.calculateAverageRating(propertyId))
                .setScale(2, RoundingMode.HALF_UP);
        property.setRatingAvg(average);
        propertyRepository.save(property);
    }

    private String normalizeComment(String comment) {
        if (comment == null || comment.isBlank()) {
            return null;
        }
        return comment.trim();
    }
}
