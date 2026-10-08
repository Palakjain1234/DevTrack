package com.devrelease.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectRequest {
    @NotBlank
    private String name;
    
    private String description;
}
