package com.devrelease.service;

import com.devrelease.dto.request.ReleaseRequest;
import com.devrelease.dto.request.StatusUpdateRequest;
import com.devrelease.dto.response.ReleaseResponse;
import com.devrelease.enums.NotificationType;
import com.devrelease.enums.ReleaseStatus;
import com.devrelease.exception.ResourceNotFoundException;
import com.devrelease.exception.UnauthorizedException;
import com.devrelease.model.Project;
import com.devrelease.model.Release;
import com.devrelease.model.User;
import com.devrelease.repository.ProjectRepository;
import com.devrelease.repository.DeploymentRepository;
import com.devrelease.repository.ReleaseRepository;
import com.devrelease.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final DeploymentRepository deploymentRepository;

    public ReleaseService(ReleaseRepository releaseRepository, ProjectRepository projectRepository,
                          UserRepository userRepository, NotificationService notificationService,
                          DeploymentRepository deploymentRepository) {
        this.releaseRepository = releaseRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.deploymentRepository = deploymentRepository;
    }

    public ReleaseResponse create(Long projectId, ReleaseRequest request, String email) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Release release = new Release();
        release.setVersion(request.getVersion());
        release.setTitle(request.getTitle());
        release.setDescription(request.getDescription());
        release.setScheduledAt(request.getScheduledAt());
        release.setStatus(ReleaseStatus.PLANNED);
        release.setProject(project);
        release.setCreatedBy(creator);
        Release saved = releaseRepository.save(release);
        notificationService.notifyProjectMembers(project,
                "New release created: " + release.getVersion() + " — " + release.getTitle(),
                NotificationType.RELEASE_CREATED);
        return toResponse(saved);
    }

    public List<ReleaseResponse> listByProject(Long projectId) {
        return releaseRepository.findByProjectId(projectId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ReleaseResponse getById(Long id) {
        return toResponse(releaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found")));
    }

    public ReleaseResponse updateStatus(Long id, StatusUpdateRequest request, String email) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found"));
        ReleaseStatus newStatus = ReleaseStatus.valueOf(request.getStatus().toUpperCase());
        if (newStatus == ReleaseStatus.ROLLED_BACK && release.getStatus() != ReleaseStatus.DEPLOYED) {
            throw new UnauthorizedException("Can only roll back a DEPLOYED release");
        }
        release.setStatus(newStatus);
        Release saved = releaseRepository.save(release);
        if (newStatus == ReleaseStatus.ROLLED_BACK) {
            notificationService.notifyProjectMembers(release.getProject(),
                    "Release " + release.getVersion() + " has been rolled back.",
                    NotificationType.RELEASE_ROLLED_BACK);
        }
        return toResponse(saved);
    }

    public void delete(Long id, String email) {
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found"));
        deploymentRepository.deleteAll(deploymentRepository.findByReleaseId(id));
        releaseRepository.delete(release);
    }

    public ReleaseResponse toResponse(Release r) {
        ReleaseResponse resp = new ReleaseResponse();
        resp.setId(r.getId());
        resp.setVersion(r.getVersion());
        resp.setTitle(r.getTitle());
        resp.setDescription(r.getDescription());
        resp.setStatus(r.getStatus());
        resp.setProjectId(r.getProject().getId());
        resp.setCreatedById(r.getCreatedBy().getId());
        resp.setScheduledAt(r.getScheduledAt());
        resp.setCreatedAt(r.getCreatedAt());
        resp.setUpdatedAt(r.getUpdatedAt());
        return resp;
    }
}
