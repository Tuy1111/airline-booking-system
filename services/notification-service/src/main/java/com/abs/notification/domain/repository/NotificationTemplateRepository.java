package com.abs.notification.domain.repository;

import com.abs.notification.domain.aggregate.NotificationTemplate;
import com.abs.notification.domain.enums.Channel;

import java.util.Optional;

public interface NotificationTemplateRepository {
    NotificationTemplate save(NotificationTemplate aggregate);
    Optional<NotificationTemplate> findByCodeAndLocaleAndChannel(
            String code,
            String locale,
            Channel channel);
}
