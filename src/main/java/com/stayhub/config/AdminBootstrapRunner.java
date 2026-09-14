package com.stayhub.config;

import com.stayhub.auth.dto.RegisterRequest;
import com.stayhub.user.EmailNormalizer;
import com.stayhub.user.User;
import com.stayhub.user.UserRepository;
import com.stayhub.user.UserRole;
import com.stayhub.user.UserStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.bootstrap.admin", name = "enabled", havingValue = "true")
public class AdminBootstrapRunner implements ApplicationRunner {

    private final AdminBootstrapProperties properties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Override
    public void run(ApplicationArguments args) {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(properties.getEmail());
        request.setPassword(properties.getPassword());
        request.setFullName(properties.getFullName());

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .findFirst()
                    .orElse("Invalid admin bootstrap configuration");
            throw new IllegalStateException("Invalid admin bootstrap configuration: " + message);
        }

        String email = EmailNormalizer.normalize(request.getEmail());
        if (userRepository.findByEmail(email).map(this::verifyExistingAdmin).orElse(false)) {
            return;
        }

        User admin = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName().trim())
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        try {
            userRepository.saveAndFlush(admin);
        } catch (DataIntegrityViolationException exception) {
            User concurrentUser = userRepository.findByEmail(email).orElseThrow(() -> exception);
            verifyExistingAdmin(concurrentUser);
        }
    }

    private boolean verifyExistingAdmin(User user) {
        if (user.getRole() != UserRole.ADMIN) {
            throw new IllegalStateException(
                    "Admin bootstrap email already belongs to a non-admin account: " + user.getEmail()
            );
        }
        return true;
    }
}
