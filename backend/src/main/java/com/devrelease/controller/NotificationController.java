package com.devrelease.controller;

import com.devrelease.dto.response.NotificationResponse;
import com.devrelease.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> list(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(notificationService.getForUser(user.getUsername()));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(Map.of("count", notificationService.countUnread(user.getUsername())));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        notificationService.markAsRead(id, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllRead(@AuthenticationPrincipal UserDetails user) {
        notificationService.markAllAsRead(user.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        notificationService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
