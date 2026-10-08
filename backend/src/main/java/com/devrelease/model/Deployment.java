package com.devrelease.model;

import com.devrelease.enums.DeploymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deployments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Deployment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_id", nullable = false)
    @JsonIgnore
    private Release release;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "environment_id", nullable = false)
    @JsonIgnore
    private Environment environment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deployed_by")
    @JsonIgnore
    private User deployedBy;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DeploymentStatus status = DeploymentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String logs;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
