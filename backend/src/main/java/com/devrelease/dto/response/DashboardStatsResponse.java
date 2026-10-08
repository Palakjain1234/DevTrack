package com.devrelease.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalProjects;
    private long totalReleases;
    private long deploymentsToday;
    private double successRate;
}
