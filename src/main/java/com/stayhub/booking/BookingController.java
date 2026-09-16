package com.stayhub.booking;

import com.stayhub.auth.UserPrincipal;
import com.stayhub.booking.dto.BookingCreateRequest;
import com.stayhub.common.exception.BusinessException;
import com.stayhub.property.PropertyService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final PropertyService propertyService;

    @GetMapping("/properties/{id}/book")
    public String showBookingPage(@PathVariable Long id,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                  @RequestParam(required = false) Integer guests,
                                  @AuthenticationPrincipal UserPrincipal principal,
                                  Model model) {
        BookingCreateRequest request = new BookingCreateRequest();
        request.setPropertyId(id);
        request.setCheckInDate(checkInDate != null ? checkInDate : LocalDate.now().plusDays(1));
        request.setCheckOutDate(checkOutDate != null ? checkOutDate : LocalDate.now().plusDays(2));
        request.setGuests(guests != null ? guests : 1);
        addBookingPageModel(model, request, principal, null);
        return "booking/booking";
    }

    @PostMapping("/bookings")
    public String createBooking(@Valid @ModelAttribute("bookingRequest") BookingCreateRequest request,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserPrincipal principal,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addBookingPageModel(model, request, principal, "Please review your booking details.");
            return "booking/booking";
        }
        try {
            var booking = bookingService.createBooking(principal.getId(), request);
            redirectAttributes.addFlashAttribute("paymentMessage", "Mock payment completed successfully.");
            return "redirect:/bookings/" + booking.getId() + "/payment";
        } catch (BusinessException exception) {
            addBookingPageModel(model, request, principal, exception.getMessage());
            return "booking/booking";
        }
    }

    @GetMapping("/bookings/{id}/payment")
    public String showPaymentSuccess(@PathVariable Long id,
                                     @AuthenticationPrincipal UserPrincipal principal,
                                     Model model) {
        model.addAttribute("booking", bookingService.getBookingForGuest(principal.getId(), id));
        if (!model.containsAttribute("paymentMessage")) {
            model.addAttribute("paymentMessage", "Mock payment already completed for this booking.");
        }
        return "booking/payment";
    }

    private void addBookingPageModel(Model model, BookingCreateRequest request, UserPrincipal principal, String errorMessage) {
        model.addAttribute("bookingRequest", request);
        model.addAttribute("property", propertyService.getPublicProperty(request.getPropertyId()));
        model.addAttribute("currentUser", principal);
        model.addAttribute("errorMessage", errorMessage);
        try {
            model.addAttribute("priceQuote", bookingService.quote(
                    request.getPropertyId(), request.getCheckInDate(), request.getCheckOutDate(), request.getGuests()));
        } catch (BusinessException exception) {
            model.addAttribute("quoteError", exception.getMessage());
        }
    }
}
