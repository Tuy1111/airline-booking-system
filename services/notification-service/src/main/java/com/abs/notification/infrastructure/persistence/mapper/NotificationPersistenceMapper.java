package com.abs.notification.infrastructure.persistence.mapper;

import com.abs.notification.domain.aggregate.NotificationAggregate;
import com.abs.notification.infrastructure.persistence.entity.NotificationEntity;

public final class NotificationPersistenceMapper {
    private NotificationPersistenceMapper() {
    }

    public static NotificationAggregate toAggregate(NotificationEntity entity) {
        return NotificationAggregate.builder()
                .id(entity.getId())
                .templateCode(entity.getTemplateCode())
                .userId(entity.getUserId())
                .channel(entity.getChannel())
                .recipient(entity.getRecipient())
                .variables(entity.getVariables())
                .status(entity.getStatus())
                .retryCount(entity.getRetryCount())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .sentAt(entity.getSentAt())
                .build();
    }

    public static NotificationEntity toEntity(NotificationAggregate aggregate) {
        return NotificationEntity.builder()
                .id(aggregate.getId())
                .templateCode(aggregate.getTemplateCode())
                .userId(aggregate.getUserId())
                .channel(aggregate.getChannel())
                .recipient(aggregate.getRecipient())
                .variables(aggregate.getVariables())
                .status(aggregate.getStatus())
                .retryCount(aggregate.getRetryCount())
                .errorMessage(aggregate.getErrorMessage())
                .createdAt(aggregate.getCreatedAt())
                .sentAt(aggregate.getSentAt())
                .build();
    }
}
