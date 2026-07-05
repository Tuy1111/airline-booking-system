package com.abs.flightsearch.application.dto;

import com.abs.flightsearch.domain.vo.FlightStatus;

import java.time.LocalDateTime;

public record FlightStatusUpdateRequest(
        FlightStatus status,
        LocalDateTime newDepartureTime,
        LocalDateTime newArrivalTime
) {}
