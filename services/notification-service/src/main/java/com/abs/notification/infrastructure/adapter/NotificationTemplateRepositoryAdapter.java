package com.abs.notification.infrastructure.adapter;

import com.abs.notification.domain.aggregate.NotificationTemplate;
import com.abs.notification.domain.repository.NotificationTemplateRepository;
import com.abs.notification.domain.enums.Channel;
import com.abs.notification.infrastructure.persistence.NotificationTemplateJpaRepository;
import com.abs.notification.infrastructure.persistence.mapper.NotificationTemplatePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationTemplateRepositoryAdapter implements NotificationTemplateRepository {
    private final NotificationTemplateJpaRepository repository;

    @Override
    public NotificationTemplate save(NotificationTemplate aggregate) {
        return NotificationTemplatePersistenceMapper.toAggregate(
                repository.save(NotificationTemplatePersistenceMapper.toEntity(aggregate)));
    }

    @Override
    public Optional<NotificationTemplate> findByCodeAndLocaleAndChannel(
            String code,
            String locale,
            Channel channel) {
        return repository.findByCodeAndLocaleAndChannel(code, locale, channel)
                .map(NotificationTemplatePersistenceMapper::toAggregate);
    }
}
