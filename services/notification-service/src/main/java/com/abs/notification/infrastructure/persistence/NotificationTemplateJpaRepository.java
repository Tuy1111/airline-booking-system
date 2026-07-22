package com.abs.notification.infrastructure.persistence;

import com.abs.notification.domain.enums.Channel;
import com.abs.notification.domain.vo.NotificationTemplateId;
import com.abs.notification.infrastructure.persistence.entity.NotificationTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTemplateJpaRepository extends JpaRepository<NotificationTemplateEntity, NotificationTemplateId> {
    Optional<NotificationTemplateEntity> findByCodeAndLocaleAndChannel(String code, String locale, Channel channel);
}
