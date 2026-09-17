package com.stayhub;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.stayhub.auth.AuthService;
import com.stayhub.auth.dto.RegisterRequest;
import com.stayhub.common.exception.DuplicateEmailException;
import com.stayhub.support.PostgreSqlIntegrationTest;
import com.stayhub.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

class StayhubApplicationTest extends PostgreSqlIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUsers() {
        userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void registrationPersistsCanonicalEmailAndRejectsCaseVariant() {
        authService.requestRegistrationOtp(request(" User@Example.COM "));
        authService.verifyRegistration("user@example.com", pendingOtp("user@example.com"));

        assertThat(userRepository.findByEmail("user@example.com")).isPresent();
        assertThatThrownBy(() -> authService.requestRegistrationOtp(request("USER@example.com")))
                .isInstanceOf(DuplicateEmailException.class);
    }

    private String pendingOtp(String email) {
        Object pending = ((java.util.Map<?, ?>) ReflectionTestUtils.getField(authService, "pendingRegistrations")).get(email);
        return pending.toString().replaceAll(".*otp=([0-9]{6}).*", "$1");
    }

    private RegisterRequest request(String email) {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword("password123");
        request.setConfirmPassword("password123");
        request.setFullName("Test User");
        request.setUsername("testuser");
        return request;
    }
}
