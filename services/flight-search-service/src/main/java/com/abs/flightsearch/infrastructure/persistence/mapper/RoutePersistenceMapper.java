package com.abs.flightsearch.infrastructure.persistence.mapper;

import com.abs.flightsearch.domain.aggregate.RouteAggregate;
import com.abs.flightsearch.infrastructure.persistence.entity.RouteEntity;

public final class RoutePersistenceMapper {
    private RoutePersistenceMapper() {
    }

    public static RouteAggregate toAggregate(RouteEntity entity) {
        return RouteAggregate.builder()
                .id(entity.getId())
                .fromAirport(AirportPersistenceMapper.toAggregate(entity.getFromAirport()))
                .toAirport(AirportPersistenceMapper.toAggregate(entity.getToAirport()))
                .distanceKm(entity.getDistanceKm())
                .build();
    }

    public static RouteEntity toEntity(RouteAggregate aggregate) {
        return RouteEntity.builder()
                .id(aggregate.getId())
                .fromAirport(AirportPersistenceMapper.toEntity(aggregate.getFromAirport()))
                .toAirport(AirportPersistenceMapper.toEntity(aggregate.getToAirport()))
                .distanceKm(aggregate.getDistanceKm())
                .build();
    }
}
