package com.abs.flightsearch.application.dto;

import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.vo.FlightStatus;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public record FlightSearchResponse(
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
        int availableSeats
) {
    public static FlightSearchResponse of(FlightAggregate f, SeatInventoryAggregate inv) {
        BigDecimal base = f.getBasePrice();
        BigDecimal current = base;

        if (inv != null && inv.getTotal() > 0) {
            double occupancy = (double) inv.getBooked() / inv.getTotal();
            BigDecimal multiplier = BigDecimal.valueOf(1.0 + 0.1 * occupancy);
            current = base.multiply(multiplier).setScale(0, RoundingMode.CEILING);
        }

        return new FlightSearchResponse(
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
                inv != null ? inv.getAvailable() : f.getTotalSeats()
        );
    }
}
