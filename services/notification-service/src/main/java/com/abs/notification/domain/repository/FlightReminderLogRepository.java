package com.abs.notification.domain.repository;

import com.abs.notification.domain.aggregate.FlightReminderLog;

public interface FlightReminderLogRepository {
    FlightReminderLog save(FlightReminderLog aggregate);
    boolean existsByBookingId(Long bookingId);
}
