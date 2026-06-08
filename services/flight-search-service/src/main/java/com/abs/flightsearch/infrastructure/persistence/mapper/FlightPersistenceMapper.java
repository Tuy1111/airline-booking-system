package com.abs.flightsearch.infrastructure.persistence.mapper;

import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.infrastructure.persistence.entity.FlightEntity;

public final class FlightPersistenceMapper {
    private FlightPersistenceMapper() {
    }

    public static FlightAggregate toAggregate(FlightEntity entity) {
        return FlightAggregate.builder()
                .id(entity.getId())
                .flightNo(entity.getFlightNo())
                .route(RoutePersistenceMapper.toAggregate(entity.getRoute()))
                .airline(AirlinePersistenceMapper.toAggregate(entity.getAirline()))
                .departureTime(entity.getDepartureTime())
                .arrivalTime(entity.getArrivalTime())
                .totalSeats(entity.getTotalSeats())
                .basePrice(entity.getBasePrice())
                .aircraftType(entity.getAircraftType())
                .status(entity.getStatus())
                .version(entity.getVersion())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static FlightEntity toEntity(FlightAggregate aggregate) {
        return FlightEntity.builder()
                .id(aggregate.getId())
                .flightNo(aggregate.getFlightNo())
                .route(RoutePersistenceMapper.toEntity(aggregate.getRoute()))
                .airline(AirlinePersistenceMapper.toEntity(aggregate.getAirline()))
                .departureTime(aggregate.getDepartureTime())
                .arrivalTime(aggregate.getArrivalTime())
                .totalSeats(aggregate.getTotalSeats())
                .basePrice(aggregate.getBasePrice())
                .aircraftType(aggregate.getAircraftType())
                .status(aggregate.getStatus())
                .version(aggregate.getVersion())
                .createdAt(aggregate.getCreatedAt())
                .build();
    }
}
