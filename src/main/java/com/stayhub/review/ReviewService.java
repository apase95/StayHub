package com.stayhub.review;

import com.stayhub.review.dto.ReviewCreateRequest;
import com.stayhub.review.dto.ReviewResponse;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    ReviewResponse createReview(Long guestId, Long bookingId, ReviewCreateRequest request);

    List<ReviewResponse> getPropertyReviews(Long propertyId);

    Optional<ReviewResponse> getReviewByBooking(Long bookingId);

    boolean hasReview(Long bookingId);
}
