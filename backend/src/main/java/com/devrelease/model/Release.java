package com.devrelease.model;

import com.devrelease.enums.ReleaseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "releases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReleaseStatus status = ReleaseStatus.PLANNED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    private LocalDateTime scheduledAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
