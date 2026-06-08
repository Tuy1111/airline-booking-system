package com.abs.notification.infrastructure.persistence.mapper;

import com.abs.notification.domain.aggregate.FlightReminderLogAggregate;
import com.abs.notification.infrastructure.persistence.entity.FlightReminderLogEntity;

public final class FlightReminderLogPersistenceMapper {
    private FlightReminderLogPersistenceMapper() {
    }

    public static FlightReminderLogAggregate toAggregate(FlightReminderLogEntity entity) {
        return FlightReminderLogAggregate.builder()
                .bookingId(entity.getBookingId())
                .flightId(entity.getFlightId())
                .sentAt(entity.getSentAt())
                .build();
    }

    public static FlightReminderLogEntity toEntity(FlightReminderLogAggregate aggregate) {
        return FlightReminderLogEntity.builder()
                .bookingId(aggregate.getBookingId())
                .flightId(aggregate.getFlightId())
                .sentAt(aggregate.getSentAt())
                .build();
    }
}
