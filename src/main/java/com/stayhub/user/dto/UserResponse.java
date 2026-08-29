package com.stayhub.user.dto;

import java.time.Instant;
import com.stayhub.user.UserRole;
import com.stayhub.user.UserStatus;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private UserRole role;
    private UserStatus status;
    private Instant createdAt;
}
