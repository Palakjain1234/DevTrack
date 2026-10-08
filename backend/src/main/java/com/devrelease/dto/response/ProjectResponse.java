package com.devrelease.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private List<Long> memberIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
