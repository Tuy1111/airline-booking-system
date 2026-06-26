package com.abs.flightsearch.application.dto;

import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.vo.FlightStatus;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public record FlightDetailResponse(
        Long id,
        String flightNo,
        String airlineCode,
        String airlineName,
        String fromAirport,
        String fromCity,
        String toAirport,
        String toCity,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        BigDecimal basePrice,
        BigDecimal currentPrice,
        String aircraftType,
        FlightStatus status,
        int availableSeats,
        int totalSeats,
        int heldSeats,
        int bookedSeats
) {
    public static FlightDetailResponse of(FlightAggregate f, SeatInventoryAggregate inv) {
        BigDecimal base = f.getBasePrice();
        BigDecimal current = f.calculateCurrentPrice(inv);

        return new FlightDetailResponse(
                f.getId(),
                f.getFlightNo(),
                f.getAirline() != null ? f.getAirline().getCode() : "Unknown",
                f.getAirline() != null ? f.getAirline().getName() : "Unknown",
                f.getRoute() != null && f.getRoute().getFromAirport() != null ? f.getRoute().getFromAirport().getIataCode() : "Unknown",
                f.getRoute() != null && f.getRoute().getFromAirport() != null ? f.getRoute().getFromAirport().getCity() : "Unknown",
                f.getRoute() != null && f.getRoute().getToAirport() != null ? f.getRoute().getToAirport().getIataCode() : "Unknown",
                f.getRoute() != null && f.getRoute().getToAirport() != null ? f.getRoute().getToAirport().getCity() : "Unknown",
                f.getDepartureTime(),
                f.getArrivalTime(),
                base,
                current,
                f.getAircraftType(),
                f.getStatus(),
                inv != null ? inv.getAvailable() : f.getTotalSeats(),
                inv != null ? inv.getTotal() : f.getTotalSeats(),
                inv != null ? inv.getHeld() : 0,
                inv != null ? inv.getBooked() : 0
        );
    }
}
