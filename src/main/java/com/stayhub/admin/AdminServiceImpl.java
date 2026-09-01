package com.stayhub.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stayhub.admin.dto.DashboardStatsResponse;
import com.stayhub.user.UserRepository;
import com.stayhub.user.UserRole;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        long totalUsers = userRepository.count();
        long activeHosts = userRepository.countByRole(UserRole.HOST);
        long mockTotalBookings = 1245;
        double mockPlatformRevenue = 2450000.0;

        return DashboardStatsResponse.builder()
                .totalUsers(totalUsers)
                .activeHosts(activeHosts)
                .totalBookings(mockTotalBookings)
                .platformRevenue(mockPlatformRevenue)
                .build();
    }
}
