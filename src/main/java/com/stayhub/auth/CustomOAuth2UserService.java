package com.stayhub.auth;

import com.stayhub.user.EmailNormalizer;
import com.stayhub.user.User;
import com.stayhub.user.UserLoginProvider;
import com.stayhub.user.UserRepository;
import com.stayhub.user.UserRole;
import com.stayhub.user.UserStatus;
import java.util.Objects;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();
        User user = upsertGoogleUser(attributes);
        return UserPrincipal.create(user, attributes, "sub");
    }

    @Transactional
    public User upsertGoogleUser(Map<String, Object> attributes) {
        String email = EmailNormalizer.normalize((String) attributes.get("email"));
        String providerId = Objects.toString(attributes.get("sub"), "");
        if (email.isBlank() || providerId.isBlank()) {
            throw new OAuth2AuthenticationException("Google account did not provide email or subject identifier.");
        }
        String fullName = displayName(attributes);
        User user = userRepository.findByEmail(email).orElseGet(() -> User.builder()
                .email(email)
                .username(uniqueUsername(email))
                .passwordHash(passwordEncoder.encode(UUID.randomUUID().toString()))
                .fullName(fullName)
                .role(UserRole.GUEST)
                .status(UserStatus.ACTIVE)
                .loginProvider(UserLoginProvider.GOOGLE)
                .providerId(providerId)
                .build());

        if (user.getId() != null) {
            user.setLoginProvider(UserLoginProvider.GOOGLE);
            user.setProviderId(providerId);
            if (user.getUsername() == null || user.getUsername().isBlank()) {
                user.setUsername(uniqueUsername(email));
            }
            if (user.getFullName() == null || user.getFullName().isBlank() || "Google User".equals(user.getFullName())) {
                user.setFullName(fullName);
            }
        }
        User saved = userRepository.saveAndFlush(user);
        log.info("Provisioned Google user {} with local id {}", saved.getEmail(), saved.getId());
        return saved;
    }

    private String displayName(Map<String, Object> attributes) {
        Object name = attributes.get("name");
        if (name != null && !name.toString().isBlank()) {
            return name.toString();
        }
        return "Google User";
    }

    private String uniqueUsername(String email) {
        String base = email.substring(0, email.indexOf('@')).replaceAll("[^a-zA-Z0-9._-]", "").toLowerCase();
        if (base.length() < 3) {
            base = "user" + base;
        }
        base = base.substring(0, Math.min(base.length(), 40));
        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + suffix;
            suffix++;
        }
        return candidate;
    }
}
