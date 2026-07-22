package com.abs.booking.application.dto;

import java.math.BigDecimal;

public record SeatInfoResponse(
        Long flightId,
        String seatNo,
        String seatClass,
        String status,
        BigDecimal price
) {}
