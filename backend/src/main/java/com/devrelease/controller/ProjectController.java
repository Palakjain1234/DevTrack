package com.devrelease.controller;

import com.devrelease.dto.request.ProjectRequest;
import com.devrelease.dto.response.ProjectResponse;
import com.devrelease.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request,
                                                   @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(projectService.create(request, user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> list(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(projectService.listForUser(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(projectService.getById(id, user.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody ProjectRequest request,
                                                   @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(projectService.update(id, request, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        projectService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/members/{userId}")
    public ResponseEntity<ProjectResponse> addMember(@PathVariable Long id, @PathVariable Long userId,
                                                      @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(projectService.addMember(id, userId, user.getUsername()));
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<ProjectResponse> removeMember(@PathVariable Long id, @PathVariable Long userId,
                                                         @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(projectService.removeMember(id, userId, user.getUsername()));
    }
}
