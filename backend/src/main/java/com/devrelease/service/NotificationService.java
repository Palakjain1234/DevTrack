package com.devrelease.service;

import com.devrelease.dto.response.NotificationResponse;
import com.devrelease.enums.NotificationType;
import com.devrelease.exception.ResourceNotFoundException;
import com.devrelease.exception.UnauthorizedException;
import com.devrelease.model.Notification;
import com.devrelease.model.Project;
import com.devrelease.model.User;
import com.devrelease.repository.NotificationRepository;
import com.devrelease.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public Notification create(User user, String message, NotificationType type) {
        Notification n = new Notification();
        n.setUser(user);
        n.setMessage(message);
        n.setType(type);
        n.setRead(false);
        return notificationRepository.save(n);
    }

    public void notifyProjectMembers(Project project, String message, NotificationType type) {
        Set<User> targets = project.getMembers();
        targets.add(project.getOwner());
        targets.forEach(u -> create(u, message, type));
    }

    public List<NotificationResponse> getForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return notificationRepository.findByUserOrderByCreatedAtDesc(user)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId, String email) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if (!n.getUser().getEmail().equals(email)) throw new UnauthorizedException("Forbidden");
        n.setRead(true);
        notificationRepository.save(n);
    }

    public void markAllAsRead(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Notification> unread = notificationRepository.findByUserAndReadFalse(user);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    public void delete(Long notificationId, String email) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if (!n.getUser().getEmail().equals(email)) throw new UnauthorizedException("Forbidden");
        notificationRepository.delete(n);
    }

    public long countUnread(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return notificationRepository.findByUserAndReadFalse(user).size();
    }

    private NotificationResponse toResponse(Notification n) {
        NotificationResponse r = new NotificationResponse();
        r.setId(n.getId());
        r.setMessage(n.getMessage());
        r.setType(n.getType());
        r.setRead(n.getRead());
        r.setCreatedAt(n.getCreatedAt());
        return r;
    }
}
