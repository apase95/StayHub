package com.stayhub.auth;

import com.stayhub.auth.dto.RegisterRequest;
import com.stayhub.common.exception.DuplicateEmailException;
import com.stayhub.user.EmailNormalizer;
import com.stayhub.user.User;
import com.stayhub.user.UserRepository;
import com.stayhub.user.UserRole;
import com.stayhub.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String EMAIL_UNIQUE_CONSTRAINT = "uk_users_email_canonical";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        String email = EmailNormalizer.normalize(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName().trim())
                .role(UserRole.GUEST)
                .status(UserStatus.ACTIVE)
                .build();

        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            if (isEmailConstraintViolation(exception)) {
                throw new DuplicateEmailException();
            }
            throw exception;
        }
    }

    private boolean isEmailConstraintViolation(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof ConstraintViolationException violation
                    && isEmailConstraint(violation.getConstraintName())) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private boolean isEmailConstraint(String constraintName) {
        return constraintName != null && (
                EMAIL_UNIQUE_CONSTRAINT.equalsIgnoreCase(constraintName)
                        || "users_email_key".equalsIgnoreCase(constraintName)
                        || "uk_users_email".equalsIgnoreCase(constraintName)
        );
    }
}
