package com.devrelease.controller;

import com.devrelease.dto.request.DeploymentRequest;
import com.devrelease.dto.request.StatusUpdateRequest;
import com.devrelease.dto.response.DeploymentResponse;
import com.devrelease.service.DeploymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deployments")
public class DeploymentController {

    private final DeploymentService deploymentService;

    public DeploymentController(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @PostMapping
    public ResponseEntity<DeploymentResponse> trigger(@Valid @RequestBody DeploymentRequest request,
                                                       @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(deploymentService.trigger(request, user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<DeploymentResponse>> listAll() {
        return ResponseEntity.ok(deploymentService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeploymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(deploymentService.getById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DeploymentResponse> updateStatus(@PathVariable Long id,
                                                            @RequestBody StatusUpdateRequest request,
                                                            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(deploymentService.updateStatus(id, request, user.getUsername()));
    }

    @GetMapping("/by-release/{releaseId}")
    public ResponseEntity<List<DeploymentResponse>> byRelease(@PathVariable Long releaseId) {
        return ResponseEntity.ok(deploymentService.listByRelease(releaseId));
    }

    @GetMapping("/by-environment/{envId}")
    public ResponseEntity<List<DeploymentResponse>> byEnvironment(@PathVariable Long envId) {
        return ResponseEntity.ok(deploymentService.listByEnvironment(envId));
    }
}
