package com.abs.flightsearch.infrastructure.persistence.mapper;

import com.abs.flightsearch.domain.aggregate.AirlineAggregate;
import com.abs.flightsearch.infrastructure.persistence.entity.AirlineEntity;

public final class AirlinePersistenceMapper {
    private AirlinePersistenceMapper() {
    }

    public static AirlineAggregate toAggregate(AirlineEntity entity) {
        return AirlineAggregate.builder().code(entity.getCode()).name(entity.getName()).build();
    }

    public static AirlineEntity toEntity(AirlineAggregate aggregate) {
        return AirlineEntity.builder().code(aggregate.getCode()).name(aggregate.getName()).build();
    }
}
