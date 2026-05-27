package com.abs.notification.infrastructure.persistence;

import com.abs.notification.domain.FlightReminderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightReminderLogRepository extends JpaRepository<FlightReminderLog, Long> {
    boolean existsByBookingId(Long bookingId);
}
