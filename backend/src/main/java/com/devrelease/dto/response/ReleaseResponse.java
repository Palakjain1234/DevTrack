package com.devrelease.dto.response;

import com.devrelease.enums.ReleaseStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReleaseResponse {
    private Long id;
    private String version;
    private String title;
    private String description;
    private ReleaseStatus status;
    private Long projectId;
    private Long createdById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
