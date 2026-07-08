package com.abs.flightsearch.application.dto;

import com.abs.flightsearch.domain.aggregate.RouteAggregate;

public record RouteResponse(
        Long id,
        String fromAirportCode,
        String fromAirportName,
        String fromCity,
        String toAirportCode,
        String toAirportName,
        String toCity,
        Integer distanceKm
) {
    public static RouteResponse of(RouteAggregate r) {
        return new RouteResponse(
                r.getId(),
                r.getFromAirport() != null ? r.getFromAirport().getIataCode() : null,
                r.getFromAirport() != null ? r.getFromAirport().getName() : null,
                r.getFromAirport() != null ? r.getFromAirport().getCity() : null,
                r.getToAirport() != null ? r.getToAirport().getIataCode() : null,
                r.getToAirport() != null ? r.getToAirport().getName() : null,
                r.getToAirport() != null ? r.getToAirport().getCity() : null,
                r.getDistanceKm()
        );
    }
}
