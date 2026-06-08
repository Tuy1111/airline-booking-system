package com.abs.notification.domain.repository;

import com.abs.notification.domain.aggregate.NotificationAggregate;
import com.abs.notification.domain.vo.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    NotificationAggregate save(NotificationAggregate aggregate);
    Optional<NotificationAggregate> findById(Long id);
    Page<NotificationAggregate> findAll(Pageable pageable);
    Page<NotificationAggregate> findByUserId(Long userId, Pageable pageable);
    List<NotificationAggregate> findByStatus(NotificationStatus status, Pageable pageable);
}
