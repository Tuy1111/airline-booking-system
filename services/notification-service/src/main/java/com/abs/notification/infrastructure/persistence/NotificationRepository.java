package com.abs.notification.infrastructure.persistence;

import com.abs.notification.domain.Notification;
import com.abs.notification.domain.NotificationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStatus(NotificationStatus status, Pageable pageable);
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
