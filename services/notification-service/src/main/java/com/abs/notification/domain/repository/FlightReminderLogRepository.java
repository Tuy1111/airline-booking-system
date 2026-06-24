package com.abs.notification.domain.repository;

import com.abs.notification.domain.aggregate.FlightReminderLogAggregate;

public interface FlightReminderLogRepository {
    FlightReminderLogAggregate save(FlightReminderLogAggregate aggregate);
    boolean existsByBookingId(Long bookingId);
}
