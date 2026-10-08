package com.devrelease.dto.request;

import com.devrelease.enums.EnvironmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnvironmentRequest {
    @NotBlank
    private String name;
    
    @NotNull
    private EnvironmentType type;
    
    @NotBlank
    private String url;
}
