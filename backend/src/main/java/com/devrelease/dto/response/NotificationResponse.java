package com.devrelease.dto.response;

import com.devrelease.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private String message;
    private NotificationType type;
    @JsonProperty("read")
    private Boolean read;
    private LocalDateTime createdAt;
}
