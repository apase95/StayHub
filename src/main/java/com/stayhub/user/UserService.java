package com.stayhub.user;

import java.util.List;

import com.stayhub.auth.dto.RegisterRequest;
import com.stayhub.user.dto.UpdateProfileRequest;


public interface UserService {
    
    User register(RegisterRequest request);
    User findByEmail(String email);
    User findById(Long id);
    void changePassword(Long userId, String oldPassword, String newPassword);
    User updateProfile(Long userId, UpdateProfileRequest request);
    List<User> getAllUsers();
    void lockUser(Long userId);
    void unlockUser(Long userId);
}
