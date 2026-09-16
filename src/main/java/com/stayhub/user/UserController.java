package com.stayhub.user;

import com.stayhub.auth.UserPrincipal;
import com.stayhub.user.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String showProfile(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        var user = userService.findById(principal.getId());
        if (!model.containsAttribute("profileRequest")) {
            UpdateProfileRequest request = new UpdateProfileRequest();
            request.setFullName(user.getFullName());
            request.setPhone(user.getPhone());
            model.addAttribute("profileRequest", request);
        }
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping
    public String updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                @Valid @ModelAttribute("profileRequest") UpdateProfileRequest request,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.findById(principal.getId()));
            return "user/profile";
        }
        userService.updateProfile(principal.getId(), request);
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully.");
        return "redirect:/profile";
    }
}
