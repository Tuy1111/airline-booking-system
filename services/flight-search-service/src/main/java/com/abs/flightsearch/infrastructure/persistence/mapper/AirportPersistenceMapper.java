package com.abs.flightsearch.infrastructure.persistence.mapper;

import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import com.abs.flightsearch.infrastructure.persistence.entity.AirportEntity;

public final class AirportPersistenceMapper {
    private AirportPersistenceMapper() {
    }

    public static AirportAggregate toAggregate(AirportEntity entity) {
        return AirportAggregate.builder()
                .iataCode(entity.getIataCode())
                .name(entity.getName())
                .city(entity.getCity())
                .country(entity.getCountry())
                .build();
    }

    public static AirportEntity toEntity(AirportAggregate aggregate) {
        return AirportEntity.builder()
                .iataCode(aggregate.getIataCode())
                .name(aggregate.getName())
                .city(aggregate.getCity())
                .country(aggregate.getCountry())
                .build();
    }
}
