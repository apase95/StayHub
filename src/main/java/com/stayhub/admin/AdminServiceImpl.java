package com.stayhub.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stayhub.admin.dto.DashboardStatsResponse;
import com.stayhub.user.UserRepository;
import com.stayhub.user.UserRole;
import com.stayhub.user.UserStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        long totalUsers = userRepository.count();
        long activeHosts = userRepository.countByRoleAndStatus(UserRole.HOST, UserStatus.ACTIVE);

        return DashboardStatsResponse.builder()
                .totalUsers(totalUsers)
                .activeHosts(activeHosts)
                .totalBookings(0)
                .platformRevenue(0.0)
                .build();
    }
}
