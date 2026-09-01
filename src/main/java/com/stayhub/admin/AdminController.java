package com.stayhub.admin;

import com.stayhub.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;
    private final UserService userService;

    @GetMapping("/bookings")
    public String viewDashboardAndBookings(Model model) {
        model.addAttribute("stats", adminService.getDashboardStats());
        return "admin/bookings";
    }
    
    @GetMapping("/users")
    public String viewUser(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }
    
}
