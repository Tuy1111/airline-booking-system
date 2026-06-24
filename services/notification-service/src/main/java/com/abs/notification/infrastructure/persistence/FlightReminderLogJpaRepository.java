package com.abs.notification.infrastructure.persistence;

import com.abs.notification.infrastructure.persistence.entity.FlightReminderLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightReminderLogJpaRepository extends JpaRepository<FlightReminderLogEntity, Long> {
    boolean existsByBookingId(Long bookingId);
}
