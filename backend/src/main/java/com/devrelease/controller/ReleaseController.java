package com.devrelease.controller;

import com.devrelease.dto.request.ReleaseRequest;
import com.devrelease.dto.request.StatusUpdateRequest;
import com.devrelease.dto.response.ReleaseResponse;
import com.devrelease.service.ReleaseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/releases")
public class ReleaseController {

    private final ReleaseService releaseService;

    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @PostMapping
    public ResponseEntity<ReleaseResponse> create(@PathVariable Long projectId,
                                                   @Valid @RequestBody ReleaseRequest request,
                                                   @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(releaseService.create(projectId, request, user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<ReleaseResponse>> list(@PathVariable Long projectId) {
        return ResponseEntity.ok(releaseService.listByProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReleaseResponse> getById(@PathVariable Long projectId, @PathVariable Long id) {
        return ResponseEntity.ok(releaseService.getById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReleaseResponse> updateStatus(@PathVariable Long projectId,
                                                         @PathVariable Long id,
                                                         @RequestBody StatusUpdateRequest request,
                                                         @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(releaseService.updateStatus(id, request, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long projectId,
                                        @PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails user) {
        releaseService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
