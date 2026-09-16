package com.stayhub.review.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long bookingId;
    private Long propertyId;
    private Long guestId;
    private String guestName;
    private Short rating;
    private String comment;
    private Instant createdAt;
}
