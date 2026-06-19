package com.abs.notification.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightReminderLog {
    private Long bookingId;
    private Long flightId;
    private LocalDateTime sentAt;
}
