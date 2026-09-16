package com.stayhub.admin;

import com.stayhub.common.response.ApiResponse;
import com.stayhub.user.UserRole;
import com.stayhub.user.UserService;
import com.stayhub.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final UserService userService;

    @PostMapping("/users/{id}/lock")
    public ApiResponse<Void> lockUser(@PathVariable Long id) {
        userService.lockUser(id);
        return ApiResponse.success(null, "User locked successfully");
    }

    @PostMapping("/users/{id}/unlock")
    public ApiResponse<Void> unlockUser(@PathVariable Long id) {
        userService.unlockUser(id);
        return ApiResponse.success(null, "User unlocked successfully");
    }

    @PostMapping("/users/{id}/role")
    public ApiResponse<UserResponse> changeRole(@PathVariable Long id, @RequestParam UserRole role) {
        return ApiResponse.success(userService.changeRole(id, role), "User role updated successfully");
    }
}
