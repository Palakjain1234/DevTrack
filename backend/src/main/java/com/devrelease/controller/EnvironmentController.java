package com.devrelease.controller;

import com.devrelease.dto.request.EnvironmentRequest;
import com.devrelease.dto.response.EnvironmentResponse;
import com.devrelease.service.EnvironmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/environments")
public class EnvironmentController {

    private final EnvironmentService environmentService;

    public EnvironmentController(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @PostMapping
    public ResponseEntity<EnvironmentResponse> create(@PathVariable Long projectId,
                                                       @Valid @RequestBody EnvironmentRequest request) {
        return ResponseEntity.ok(environmentService.create(projectId, request));
    }

    @GetMapping
    public ResponseEntity<List<EnvironmentResponse>> list(@PathVariable Long projectId) {
        return ResponseEntity.ok(environmentService.listByProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvironmentResponse> getById(@PathVariable Long projectId, @PathVariable Long id) {
        return ResponseEntity.ok(environmentService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnvironmentResponse> update(@PathVariable Long projectId, @PathVariable Long id,
                                                       @Valid @RequestBody EnvironmentRequest request) {
        return ResponseEntity.ok(environmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long projectId, @PathVariable Long id) {
        environmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
