package com.stayhub.auth.dto;

import com.stayhub.user.EmailNormalizer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyRegistrationRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank(message = "OTP is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be 6 digits")
    private String otp;

    public void setEmail(String email) {
        this.email = EmailNormalizer.normalize(email);
    }
}
