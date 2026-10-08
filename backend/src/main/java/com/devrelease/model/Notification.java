package com.devrelease.model;

import com.devrelease.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "is_read")
    @Builder.Default
    private Boolean read = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
