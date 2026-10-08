package com.devrelease.service;

import com.devrelease.dto.request.DeploymentRequest;
import com.devrelease.dto.request.StatusUpdateRequest;
import com.devrelease.dto.response.DeploymentResponse;
import com.devrelease.enums.DeploymentStatus;
import com.devrelease.enums.NotificationType;
import com.devrelease.enums.ReleaseStatus;
import com.devrelease.exception.ResourceNotFoundException;
import com.devrelease.exception.UnauthorizedException;
import com.devrelease.model.Deployment;
import com.devrelease.model.Environment;
import com.devrelease.model.Release;
import com.devrelease.model.User;
import com.devrelease.repository.DeploymentRepository;
import com.devrelease.repository.EnvironmentRepository;
import com.devrelease.repository.ReleaseRepository;
import com.devrelease.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeploymentService {

    private final DeploymentRepository deploymentRepository;
    private final ReleaseRepository releaseRepository;
    private final EnvironmentRepository environmentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public DeploymentService(DeploymentRepository deploymentRepository,
                             ReleaseRepository releaseRepository,
                             EnvironmentRepository environmentRepository,
                             UserRepository userRepository,
                             NotificationService notificationService) {
        this.deploymentRepository = deploymentRepository;
        this.releaseRepository = releaseRepository;
        this.environmentRepository = environmentRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public DeploymentResponse trigger(DeploymentRequest request, String email) {
        Release release = releaseRepository.findById(request.getReleaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Release not found"));
        if (release.getStatus() == ReleaseStatus.DEPLOYED || release.getStatus() == ReleaseStatus.FAILED
                || release.getStatus() == ReleaseStatus.ROLLED_BACK) {
            throw new UnauthorizedException("Cannot deploy a release with status: " + release.getStatus());
        }
        Environment environment = environmentRepository.findById(request.getEnvironmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Environment not found"));
        User deployer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Deployment deployment = new Deployment();
        deployment.setRelease(release);
        deployment.setEnvironment(environment);
        deployment.setDeployedBy(deployer);
        deployment.setStatus(DeploymentStatus.RUNNING);
        deployment.setStartedAt(LocalDateTime.now());
        deployment.setLogs("[INFO] Deployment triggered by " + deployer.getName() + "\n[INFO] Running pipeline...");
        Deployment saved = deploymentRepository.save(deployment);

        release.setStatus(ReleaseStatus.IN_PROGRESS);
        releaseRepository.save(release);

        notificationService.notifyProjectMembers(release.getProject(),
                "Deployment triggered for release " + release.getVersion() + " to " + environment.getName(),
                NotificationType.DEPLOYMENT_TRIGGERED);

        return toResponse(saved);
    }

    public DeploymentResponse updateStatus(Long deploymentId, StatusUpdateRequest request, String email) {
        Deployment deployment = deploymentRepository.findById(deploymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Deployment not found"));
        DeploymentStatus newStatus = DeploymentStatus.valueOf(request.getStatus().toUpperCase());
        deployment.setStatus(newStatus);

        if (newStatus == DeploymentStatus.SUCCESS || newStatus == DeploymentStatus.FAILED) {
            deployment.setCompletedAt(LocalDateTime.now());
        }

        Release release = deployment.getRelease();
        if (newStatus == DeploymentStatus.SUCCESS) {
            release.setStatus(ReleaseStatus.DEPLOYED);
            releaseRepository.save(release);
            deployment.setLogs(deployment.getLogs() + "\n[SUCCESS] Deployment completed successfully.");
            notificationService.notifyProjectMembers(release.getProject(),
                    "Deployment for release " + release.getVersion() + " succeeded on " + deployment.getEnvironment().getName(),
                    NotificationType.DEPLOYMENT_SUCCESS);
        } else if (newStatus == DeploymentStatus.FAILED) {
            release.setStatus(ReleaseStatus.FAILED);
            releaseRepository.save(release);
            deployment.setLogs(deployment.getLogs() + "\n[ERROR] Deployment failed.");
            notificationService.notifyProjectMembers(release.getProject(),
                    "Deployment for release " + release.getVersion() + " FAILED on " + deployment.getEnvironment().getName(),
                    NotificationType.DEPLOYMENT_FAILED);
        }

        return toResponse(deploymentRepository.save(deployment));
    }

    public List<DeploymentResponse> listAll() {
        return deploymentRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<DeploymentResponse> listByRelease(Long releaseId) {
        return deploymentRepository.findByReleaseId(releaseId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<DeploymentResponse> listByEnvironment(Long environmentId) {
        return deploymentRepository.findByEnvironmentId(environmentId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public DeploymentResponse getById(Long id) {
        return toResponse(deploymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deployment not found")));
    }

    private DeploymentResponse toResponse(Deployment d) {
        DeploymentResponse r = new DeploymentResponse();
        r.setId(d.getId());
        r.setReleaseId(d.getRelease().getId());
        r.setEnvironmentId(d.getEnvironment().getId());
        r.setDeployedById(d.getDeployedBy().getId());
        r.setStatus(d.getStatus());
        r.setLogs(d.getLogs());
        r.setStartedAt(d.getStartedAt());
        r.setCompletedAt(d.getCompletedAt());
        return r;
    }
}
