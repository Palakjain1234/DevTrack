package com.devrelease.service;

import com.devrelease.dto.request.ProjectRequest;
import com.devrelease.dto.response.ProjectResponse;
import com.devrelease.enums.Role;
import com.devrelease.exception.ResourceNotFoundException;
import com.devrelease.exception.UnauthorizedException;
import com.devrelease.model.Project;
import com.devrelease.model.User;
import com.devrelease.repository.DeploymentRepository;
import com.devrelease.repository.EnvironmentRepository;
import com.devrelease.repository.NotificationRepository;
import com.devrelease.repository.ProjectRepository;
import com.devrelease.repository.ReleaseRepository;
import com.devrelease.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ReleaseRepository releaseRepository;
    private final DeploymentRepository deploymentRepository;
    private final EnvironmentRepository environmentRepository;
    private final NotificationRepository notificationRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository,
                          ReleaseRepository releaseRepository, DeploymentRepository deploymentRepository,
                          EnvironmentRepository environmentRepository, NotificationRepository notificationRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.releaseRepository = releaseRepository;
        this.deploymentRepository = deploymentRepository;
        this.environmentRepository = environmentRepository;
        this.notificationRepository = notificationRepository;
    }

    public ProjectResponse create(ProjectRequest request, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOwner(owner);
        project.setMembers(new HashSet<>());
        project.getMembers().add(owner);
        return toResponse(projectRepository.save(project));
    }

    public List<ProjectResponse> listForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole() == Role.ADMIN) {
            return projectRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
        }
        List<Project> owned = projectRepository.findByOwner(user);
        List<Project> member = projectRepository.findByMembersContaining(user);
        owned.addAll(member);
        return owned.stream().distinct().map(this::toResponse).collect(Collectors.toList());
    }

    public ProjectResponse getById(Long id, String email) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        assertAccess(project, email);
        return toResponse(project);
    }

    public ProjectResponse update(Long id, ProjectRequest request, String email) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        assertOwnerOrAdmin(project, email);
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return toResponse(projectRepository.save(project));
    }

    public void delete(Long id, String email) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        assertOwnerOrAdmin(project, email);

        // 1. Delete all deployments for every release in this project
        List<Long> releaseIds = releaseRepository.findByProjectId(id)
                .stream().map(r -> r.getId()).collect(Collectors.toList());
        releaseIds.forEach(rid -> deploymentRepository.deleteAll(deploymentRepository.findByReleaseId(rid)));

        // 2. Delete all deployments for every environment in this project
        environmentRepository.findByProjectId(id)
                .forEach(env -> deploymentRepository.deleteAll(deploymentRepository.findByEnvironmentId(env.getId())));

        // 3. Delete all releases
        releaseRepository.deleteAll(releaseRepository.findByProjectId(id));

        // 4. Delete all environments
        environmentRepository.deleteAll(environmentRepository.findByProjectId(id));

        // 5. Clear members join table so JPA can delete the project
        project.getMembers().clear();
        projectRepository.save(project);

        // 6. Delete the project
        projectRepository.delete(project);
    }

    public ProjectResponse addMember(Long projectId, Long userId, String email) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        assertOwnerOrAdmin(project, email);
        User member = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        project.getMembers().add(member);
        return toResponse(projectRepository.save(project));
    }

    public ProjectResponse removeMember(Long projectId, Long userId, String email) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        assertOwnerOrAdmin(project, email);
        User member = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        project.getMembers().remove(member);
        return toResponse(projectRepository.save(project));
    }

    private void assertAccess(Project project, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        boolean isMember = project.getMembers().stream().anyMatch(m -> m.getEmail().equals(email));
        if (user.getRole() != Role.ADMIN && !isMember) throw new UnauthorizedException("Access denied");
    }

    private void assertOwnerOrAdmin(Project project, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getRole() != Role.ADMIN && !project.getOwner().getEmail().equals(email))
            throw new UnauthorizedException("Only owner or admin");
    }

    private ProjectResponse toResponse(Project p) {
        ProjectResponse r = new ProjectResponse();
        r.setId(p.getId());
        r.setName(p.getName());
        r.setDescription(p.getDescription());
        r.setOwnerId(p.getOwner().getId());
        r.setMemberIds(p.getMembers().stream().map(User::getId).collect(Collectors.toList()));
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }
}
