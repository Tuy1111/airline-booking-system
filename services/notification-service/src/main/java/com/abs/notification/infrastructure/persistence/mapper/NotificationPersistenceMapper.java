package com.abs.notification.infrastructure.persistence.mapper;

import com.abs.notification.domain.aggregate.Notification;
import com.abs.notification.domain.vo.Recipient;
import com.abs.notification.infrastructure.persistence.entity.NotificationEntity;

public final class NotificationPersistenceMapper {
    private NotificationPersistenceMapper() {
    }

    public static Notification toAggregate(NotificationEntity entity) {
        return Notification.builder()
                .id(entity.getId())
                .templateCode(entity.getTemplateCode())
                .userId(entity.getUserId())
                .recipient(Recipient.of(entity.getChannel(), entity.getRecipient()))
                .variables(entity.getVariables())
                .status(entity.getStatus())
                .retryCount(entity.getRetryCount())
                .errorMessage(entity.getErrorMessage())
                .dedupKey(entity.getDedupKey())
                .createdAt(entity.getCreatedAt())
                .sentAt(entity.getSentAt())
                .build();
    }

    public static NotificationEntity toEntity(Notification aggregate) {
        return NotificationEntity.builder()
                .id(aggregate.getId())
                .templateCode(aggregate.getTemplateCode())
                .userId(aggregate.getUserId())
                .channel(aggregate.getRecipient().channel())
                .recipient(aggregate.getRecipient().address())
                .variables(aggregate.getVariables())
                .status(aggregate.getStatus())
                .retryCount(aggregate.getRetryCount())
                .errorMessage(aggregate.getErrorMessage())
                .dedupKey(aggregate.getDedupKey())
                .createdAt(aggregate.getCreatedAt())
                .sentAt(aggregate.getSentAt())
                .build();
    }
}
