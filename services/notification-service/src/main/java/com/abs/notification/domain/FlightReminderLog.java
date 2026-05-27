package com.abs.notification.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "flight_reminder_log")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FlightReminderLog {

    @Id
    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "flight_id", nullable = false)
    private Long flightId;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;
}
