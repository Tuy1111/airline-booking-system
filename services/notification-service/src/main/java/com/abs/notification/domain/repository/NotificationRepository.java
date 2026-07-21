package com.abs.notification.domain.repository;

import com.abs.notification.domain.aggregate.Notification;
import com.abs.notification.domain.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification aggregate);
    Optional<Notification> findById(Long id);
    Optional<Notification> findByDedupKey(String dedupKey);
    Page<Notification> findAll(Pageable pageable);
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    Optional<Notification> findByIdAndUserId(Long id, Long userId);
    long countUnreadByUserId(Long userId);
    int markAllReadByUserId(Long userId, java.time.LocalDateTime readAt);
    List<Notification> findByStatus(NotificationStatus status, Pageable pageable);
}
