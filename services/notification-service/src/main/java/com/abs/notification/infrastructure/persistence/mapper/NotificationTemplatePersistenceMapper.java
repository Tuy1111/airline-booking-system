package com.abs.notification.infrastructure.persistence.mapper;

import com.abs.notification.domain.aggregate.NotificationTemplate;
import com.abs.notification.infrastructure.persistence.entity.NotificationTemplateEntity;

public final class NotificationTemplatePersistenceMapper {
    private NotificationTemplatePersistenceMapper() {
    }

    public static NotificationTemplate toAggregate(NotificationTemplateEntity entity) {
        return NotificationTemplate.builder()
                .code(entity.getCode())
                .locale(entity.getLocale())
                .channel(entity.getChannel())
                .subject(entity.getSubject())
                .body(entity.getBody())
                .build();
    }

    public static NotificationTemplateEntity toEntity(NotificationTemplate aggregate) {
        return NotificationTemplateEntity.builder()
                .code(aggregate.getCode())
                .locale(aggregate.getLocale())
                .channel(aggregate.getChannel())
                .subject(aggregate.getSubject())
                .body(aggregate.getBody())
                .build();
    }
}
