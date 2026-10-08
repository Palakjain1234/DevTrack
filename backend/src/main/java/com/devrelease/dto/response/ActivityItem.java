package com.devrelease.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityItem {
    /** "DEPLOYMENT" or "RELEASE" */
    private String type;

    /** Human-readable description, e.g. "Deploy #3 → v1.2 on Production" */
    private String message;

    /** Status value e.g. RUNNING, SUCCESS, FAILED, PLANNED, DEPLOYED */
    private String status;

    /** Navigation id — deployment id or release id */
    private Long referenceId;

    private LocalDateTime timestamp;
}
