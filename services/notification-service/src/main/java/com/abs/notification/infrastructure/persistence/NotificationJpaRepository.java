package com.abs.notification.infrastructure.persistence;

import com.abs.notification.domain.enums.NotificationStatus;
import com.abs.notification.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByStatus(NotificationStatus status, Pageable pageable);
    Page<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Optional<NotificationEntity> findByDedupKey(String dedupKey);
}
