package com.devrelease.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeploymentRequest {
    @NotNull
    private Long releaseId;
    
    @NotNull
    private Long environmentId;
}
