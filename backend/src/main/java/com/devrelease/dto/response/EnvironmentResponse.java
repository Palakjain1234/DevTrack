package com.devrelease.dto.response;

import com.devrelease.enums.EnvironmentType;
import com.devrelease.enums.EnvironmentStatus;
import lombok.Data;

@Data
public class EnvironmentResponse {
    private Long id;
    private String name;
    private EnvironmentType type;
    private String url;
    private EnvironmentStatus status;
    private Long projectId;
}
