package com.devrelease.dto.response;

import com.devrelease.enums.DeploymentStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeploymentResponse {
    private Long id;
    private Long releaseId;
    private Long environmentId;
    private Long deployedById;
    private DeploymentStatus status;
    private String logs;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
