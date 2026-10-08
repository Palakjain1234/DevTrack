package com.devrelease.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReleaseRequest {
    @NotBlank
    private String version;

    @NotBlank
    private String title;

    private String description;

    private LocalDateTime scheduledAt;
}
