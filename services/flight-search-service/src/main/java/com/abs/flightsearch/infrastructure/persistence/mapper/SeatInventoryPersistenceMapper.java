package com.abs.flightsearch.infrastructure.persistence.mapper;

import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;
import com.abs.flightsearch.infrastructure.persistence.entity.FlightEntity;
import com.abs.flightsearch.infrastructure.persistence.entity.SeatInventoryEntity;

public final class SeatInventoryPersistenceMapper {
    private SeatInventoryPersistenceMapper() {
    }

    public static SeatInventoryAggregate toAggregate(SeatInventoryEntity entity) {
        return SeatInventoryAggregate.builder()
                .flightId(entity.getFlightId())
                .total(entity.getTotal())
                .available(entity.getAvailable())
                .held(entity.getHeld())
                .booked(entity.getBooked())
                .version(entity.getVersion())
                .build();
    }

    public static SeatInventoryEntity toEntity(SeatInventoryAggregate aggregate) {
        FlightEntity flight = new FlightEntity();
        flight.setId(aggregate.getFlightId());
        return SeatInventoryEntity.builder()
                .flightId(aggregate.getFlightId())
                .flight(flight)
                .total(aggregate.getTotal())
                .available(aggregate.getAvailable())
                .held(aggregate.getHeld())
                .booked(aggregate.getBooked())
                .version(aggregate.getVersion())
                .build();
    }
}
