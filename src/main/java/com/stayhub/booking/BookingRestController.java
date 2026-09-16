package com.stayhub.booking;

import com.stayhub.auth.UserPrincipal;
import com.stayhub.booking.dto.AvailabilityRequest;
import com.stayhub.booking.dto.AvailabilityResponse;
import com.stayhub.booking.dto.BookingResponse;
import com.stayhub.common.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/my")
    public ApiResponse<List<BookingResponse>> getMyBookings(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(bookingService.getGuestBookings(principal.getId()), "Bookings retrieved successfully.");
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<BookingResponse> cancelBooking(@PathVariable Long id,
                                                      @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(bookingService.cancelBooking(principal.getId(), id), "Booking cancelled successfully.");
    }

    @GetMapping("/host/requests")
    public ApiResponse<List<BookingResponse>> getHostBookingRequests(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(bookingService.getBookingRequestsByHost(principal.getId()), "Booking requests retrieved successfully.");
    }

    @PostMapping("/host/{id}/accept")
    public ApiResponse<BookingResponse> acceptBooking(@PathVariable Long id,
                                                      @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(bookingService.acceptBooking(principal.getId(), id), "Booking accepted successfully.");
    }

    @PostMapping("/host/{id}/reject")
    public ApiResponse<BookingResponse> rejectBooking(@PathVariable Long id,
                                                      @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(bookingService.rejectBooking(principal.getId(), id), "Booking rejected successfully.");
    }
}
