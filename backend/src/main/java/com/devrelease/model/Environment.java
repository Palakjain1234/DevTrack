package com.devrelease.model;

import com.devrelease.enums.EnvironmentStatus;
import com.devrelease.enums.EnvironmentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "environments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Environment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnvironmentType type;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EnvironmentStatus status = EnvironmentStatus.ACTIVE;
}
