package com.abs.flightsearch.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteAggregate {
    private Long id;
    private AirportAggregate fromAirport;
    private AirportAggregate toAirport;
    private Integer distanceKm;
}
