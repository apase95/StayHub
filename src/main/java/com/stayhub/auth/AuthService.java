package com.stayhub.auth;

import com.stayhub.auth.dto.RegisterRequest;

public interface AuthService {

    void requestRegistrationOtp(RegisterRequest request);

    void verifyRegistration(String email, String otp);
}
