package com.abs.notification.infrastructure.adapter;

import com.abs.notification.domain.aggregate.FlightReminderLog;
import com.abs.notification.domain.repository.FlightReminderLogRepository;
import com.abs.notification.infrastructure.persistence.FlightReminderLogJpaRepository;
import com.abs.notification.infrastructure.persistence.mapper.FlightReminderLogPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightReminderLogRepositoryAdapter implements FlightReminderLogRepository {
    private final FlightReminderLogJpaRepository repository;

    @Override
    public FlightReminderLog save(FlightReminderLog aggregate) {
        return FlightReminderLogPersistenceMapper.toAggregate(
                repository.save(FlightReminderLogPersistenceMapper.toEntity(aggregate)));
    }

    @Override
    public boolean existsByBookingId(Long bookingId) {
        return repository.existsByBookingId(bookingId);
    }
}
