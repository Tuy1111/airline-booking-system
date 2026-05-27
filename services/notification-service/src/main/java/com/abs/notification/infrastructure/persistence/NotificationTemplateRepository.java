package com.abs.notification.infrastructure.persistence;

import com.abs.notification.domain.Channel;
import com.abs.notification.domain.NotificationTemplate;
import com.abs.notification.domain.NotificationTemplateId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, NotificationTemplateId> {
    Optional<NotificationTemplate> findByCodeAndLocaleAndChannel(String code, String locale, Channel channel);
}
