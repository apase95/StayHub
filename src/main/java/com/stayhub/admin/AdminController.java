package com.stayhub.admin;

import com.stayhub.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    public String viewUser(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "20") int size,
                           Model model) {
        model.addAttribute("userPage", userService.getUsers(page, size));
        return "admin/users";
    }
    
}
