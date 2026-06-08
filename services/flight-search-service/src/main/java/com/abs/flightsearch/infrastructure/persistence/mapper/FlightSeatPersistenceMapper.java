package com.abs.flightsearch.infrastructure.persistence.mapper;

import com.abs.flightsearch.domain.aggregate.FlightSeatAggregate;
import com.abs.flightsearch.infrastructure.persistence.entity.FlightSeatEntity;

public final class FlightSeatPersistenceMapper {
    private FlightSeatPersistenceMapper() {
    }

    public static FlightSeatAggregate toAggregate(FlightSeatEntity entity) {
        return FlightSeatAggregate.builder()
                .flightId(entity.getFlightId())
                .seatNo(entity.getSeatNo())
                .seatClass(entity.getSeatClass())
                .status(entity.getStatus())
                .priceFactor(entity.getPriceFactor())
                .build();
    }

    public static FlightSeatEntity toEntity(FlightSeatAggregate aggregate) {
        return FlightSeatEntity.builder()
                .flightId(aggregate.getFlightId())
                .seatNo(aggregate.getSeatNo())
                .seatClass(aggregate.getSeatClass())
                .status(aggregate.getStatus())
                .priceFactor(aggregate.getPriceFactor())
                .build();
    }
}
