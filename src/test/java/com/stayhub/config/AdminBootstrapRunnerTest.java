package com.stayhub.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stayhub.user.User;
import com.stayhub.user.UserRepository;
import com.stayhub.user.UserRole;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AdminBootstrapRunnerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AdminBootstrapProperties properties;
    private AdminBootstrapRunner runner;

    @BeforeEach
    void setUp() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        properties = new AdminBootstrapProperties();
        properties.setEmail(" Admin@Example.com ");
        properties.setPassword("password123");
        properties.setFullName("StayHub Admin");
        runner = new AdminBootstrapRunner(properties, userRepository, passwordEncoder, validator);
    }

    @Test
    void createsCanonicalAdminWhenAccountDoesNotExist() throws Exception {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded");

        runner.run(new DefaultApplicationArguments());

        verify(userRepository).saveAndFlush(any(User.class));
    }

    @Test
    void leavesExistingAdminUnchanged() throws Exception {
        User existing = User.builder().email("admin@example.com").role(UserRole.ADMIN).build();
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(existing));

        runner.run(new DefaultApplicationArguments());

        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void refusesToPromoteExistingGuest() {
        User existing = User.builder().email("admin@example.com").role(UserRole.GUEST).build();
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> runner.run(new DefaultApplicationArguments()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("non-admin account");
    }
}
