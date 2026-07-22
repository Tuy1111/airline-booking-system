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

    public static RouteAggregate create(AirportAggregate fromAirport, AirportAggregate toAirport, Integer distanceKm) {
        if (fromAirport == null || toAirport == null) {
            throw new IllegalArgumentException("Sân bay đi và sân bay đến không được trống");
        }
        if (fromAirport.getIataCode().equals(toAirport.getIataCode())) {
            throw new IllegalArgumentException("Sân bay đi và sân bay đến không được trùng nhau");
        }
        if (distanceKm == null || distanceKm <= 0) {
            throw new IllegalArgumentException("Khoảng cách chặng bay phải lớn hơn 0");
        }
        return RouteAggregate.builder()
                .fromAirport(fromAirport)
                .toAirport(toAirport)
                .distanceKm(distanceKm)
                .build();
    }
}
