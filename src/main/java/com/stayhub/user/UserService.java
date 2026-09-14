package com.stayhub.user;

import com.stayhub.user.dto.UpdateProfileRequest;
import com.stayhub.user.dto.UserResponse;
import org.springframework.data.domain.Page;


public interface UserService {
    
    UserResponse findByEmail(String email);
    UserResponse findById(Long id);
    void changePassword(Long userId, String oldPassword, String newPassword);
    UserResponse updateProfile(Long userId, UpdateProfileRequest request);
    Page<UserResponse> getUsers(int page, int size);
    void lockUser(Long userId);
    void unlockUser(Long userId);
}
