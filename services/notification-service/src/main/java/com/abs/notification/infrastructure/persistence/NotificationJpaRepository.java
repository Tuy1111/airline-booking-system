package com.abs.notification.infrastructure.persistence;

import com.abs.notification.domain.enums.NotificationStatus;
import com.abs.notification.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByStatus(NotificationStatus status, Pageable pageable);
    Page<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Optional<NotificationEntity> findByIdAndUserId(Long id, Long userId);
    long countByUserIdAndReadAtIsNull(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update NotificationEntity n set n.readAt = :readAt " +
            "where n.userId = :userId and n.readAt is null")
    int markAllReadByUserId(@Param("userId") Long userId, @Param("readAt") LocalDateTime readAt);

    Optional<NotificationEntity> findByDedupKey(String dedupKey);
}
