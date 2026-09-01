package com.stayhub.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResponse {
    private long totalUsers;
    private long activeHosts;
    private long totalBookings;
    private double platformRevenue;
}
