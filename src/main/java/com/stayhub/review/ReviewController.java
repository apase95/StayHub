package com.stayhub.review;

import com.stayhub.auth.UserPrincipal;
import com.stayhub.common.exception.BusinessException;
import com.stayhub.review.dto.ReviewCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/bookings/{id}/reviews")
    public String createReview(@PathVariable Long id,
                               @Valid @ModelAttribute("reviewRequest") ReviewCreateRequest request,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal UserPrincipal principal,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("reviewError", "Please choose a rating from 1 to 5.");
            return "redirect:/bookings/" + id;
        }
        try {
            reviewService.createReview(principal.getId(), id, request);
            redirectAttributes.addFlashAttribute("message", "Review submitted successfully.");
        } catch (BusinessException exception) {
            redirectAttributes.addFlashAttribute("reviewError", exception.getMessage());
        }
        return "redirect:/bookings/" + id;
    }
}
