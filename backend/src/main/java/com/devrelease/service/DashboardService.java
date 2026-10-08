package com.devrelease.service;

import com.devrelease.dto.response.DashboardStatsResponse;
import com.devrelease.enums.Role;
import com.devrelease.exception.ResourceNotFoundException;
import com.devrelease.model.User;
import com.devrelease.repository.DeploymentRepository;
import com.devrelease.repository.ProjectRepository;
import com.devrelease.repository.ReleaseRepository;
import com.devrelease.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final ReleaseRepository releaseRepository;
    private final DeploymentRepository deploymentRepository;
    private final UserRepository userRepository;

    public DashboardService(ProjectRepository projectRepository,
                            ReleaseRepository releaseRepository,
                            DeploymentRepository deploymentRepository,
                            UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.releaseRepository = releaseRepository;
        this.deploymentRepository = deploymentRepository;
        this.userRepository = userRepository;
    }

    public DashboardStatsResponse getStats(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            // Admin sees global stats
            long totalProjects = projectRepository.count();
            long totalReleases = releaseRepository.count();
            long deploymentsToday = deploymentRepository.countDeploymentsToday(LocalDate.now());
            long totalDeployments = deploymentRepository.count();
            double successRate = totalDeployments == 0 ? 0.0
                    : (double) deploymentRepository.countSuccessful() / totalDeployments * 100.0;
            return new DashboardStatsResponse(totalProjects, totalReleases, deploymentsToday, successRate);
        }

        // Non-admin sees stats scoped to their projects
        List<Long> projectIds = projectRepository.findAllAccessibleByUser(user)
                .stream().map(p -> p.getId()).collect(Collectors.toList());

        if (projectIds.isEmpty()) {
            return new DashboardStatsResponse(0, 0, 0, 0.0);
        }

        long totalProjects = projectIds.size();
        long totalReleases = releaseRepository.countByProjectIdIn(projectIds);
        long deploymentsToday = deploymentRepository.countDeploymentsTodayForProjects(projectIds, LocalDate.now());
        long totalDeployments = deploymentRepository.countTotalForProjects(projectIds);
        double successRate = totalDeployments == 0 ? 0.0
                : (double) deploymentRepository.countSuccessfulForProjects(projectIds) / totalDeployments * 100.0;

        return new DashboardStatsResponse(totalProjects, totalReleases, deploymentsToday, successRate);
    }
}
