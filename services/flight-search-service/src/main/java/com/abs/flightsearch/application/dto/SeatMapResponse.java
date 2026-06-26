package com.abs.flightsearch.application.dto;

import com.abs.flightsearch.domain.aggregate.FlightSeatAggregate;
import com.abs.flightsearch.domain.vo.SeatClass;
import com.abs.flightsearch.domain.vo.SeatStatus;

import java.math.BigDecimal;

public record SeatMapResponse(
        String seatNo,
        SeatClass seatClass,
        SeatStatus status,
        BigDecimal priceFactor
) {
    public static SeatMapResponse of(FlightSeatAggregate seat) {
        return new SeatMapResponse(
                seat.getSeatNo(),
                seat.getSeatClass(),
                seat.getStatus(),
                seat.getPriceFactor()
        );
    }
}
