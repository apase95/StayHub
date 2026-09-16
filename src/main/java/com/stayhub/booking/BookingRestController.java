package com.stayhub.booking;

import com.stayhub.booking.dto.AvailabilityRequest;
import com.stayhub.booking.dto.AvailabilityResponse;
import com.stayhub.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final BookingService bookingService;

    @PostMapping("/check-availability")
    public ApiResponse<AvailabilityResponse> checkAvailability(@Valid @RequestBody AvailabilityRequest request) {
        return ApiResponse.success(bookingService.checkAvailability(request), "The selected dates are available.");
    }
}
