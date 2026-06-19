package com.abs.flightsearch.application.dto;

import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.aggregate.FlightSeatAggregate;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record SeatInfoResponse(
        Long flightId,
        String seatNo,
        String seatClass,
        String status,
        BigDecimal price
) {
    public static SeatInfoResponse of(FlightSeatAggregate seat, FlightAggregate flight, SeatInventoryAggregate inv) {
        BigDecimal price = flight.calculateSeatPrice(seat, inv);

        return new SeatInfoResponse(
                seat.getFlightId(),
                seat.getSeatNo(),
                seat.getSeatClass().name(),
                seat.getStatus().name(),
                price
        );
    }
}
