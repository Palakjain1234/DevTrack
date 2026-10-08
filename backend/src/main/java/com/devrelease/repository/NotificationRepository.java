package com.devrelease.repository;

import com.devrelease.model.Notification;
import com.devrelease.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    List<Notification> findByUserAndReadFalse(User user);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.user = :user")
    void markAllReadForUser(@Param("user") User user);
}
