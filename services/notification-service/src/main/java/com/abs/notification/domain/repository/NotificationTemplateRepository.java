package com.abs.notification.domain.repository;

import com.abs.notification.domain.aggregate.NotificationTemplateAggregate;
import com.abs.notification.domain.vo.Channel;

import java.util.Optional;

public interface NotificationTemplateRepository {
    NotificationTemplateAggregate save(NotificationTemplateAggregate aggregate);
    Optional<NotificationTemplateAggregate> findByCodeAndLocaleAndChannel(
            String code,
            String locale,
            Channel channel);
}
