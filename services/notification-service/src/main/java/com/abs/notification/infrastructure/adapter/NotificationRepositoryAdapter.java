package com.abs.notification.infrastructure.adapter;

import com.abs.notification.domain.aggregate.Notification;
import com.abs.notification.domain.repository.NotificationRepository;
import com.abs.notification.domain.enums.NotificationStatus;
import com.abs.notification.infrastructure.persistence.NotificationJpaRepository;
import com.abs.notification.infrastructure.persistence.mapper.NotificationPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {
    private final NotificationJpaRepository repository;

    @Override
    public Notification save(Notification aggregate) {
        return NotificationPersistenceMapper.toAggregate(
                repository.save(NotificationPersistenceMapper.toEntity(aggregate)));
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return repository.findById(id).map(NotificationPersistenceMapper::toAggregate);
    }

    @Override
    public Optional<Notification> findByDedupKey(String dedupKey) {
        return repository.findByDedupKey(dedupKey).map(NotificationPersistenceMapper::toAggregate);
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(NotificationPersistenceMapper::toAggregate);
    }

    @Override
    public Page<Notification> findByUserId(Long userId, Pageable pageable) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(NotificationPersistenceMapper::toAggregate);
    }

    @Override
    public Optional<Notification> findByIdAndUserId(Long id, Long userId) {
        return repository.findByIdAndUserId(id, userId)
                .map(NotificationPersistenceMapper::toAggregate);
    }

    @Override
    public long countUnreadByUserId(Long userId) {
        return repository.countByUserIdAndReadAtIsNull(userId);
    }

    @Override
    @Transactional
    public int markAllReadByUserId(Long userId, LocalDateTime readAt) {
        return repository.markAllReadByUserId(userId, readAt);
    }

    @Override
    public List<Notification> findByStatus(
            NotificationStatus status,
            Pageable pageable) {
        return repository.findByStatus(status, pageable).stream()
                .map(NotificationPersistenceMapper::toAggregate)
                .toList();
    }
}
