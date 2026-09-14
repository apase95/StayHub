package com.stayhub.auth;

import com.stayhub.auth.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);
}
