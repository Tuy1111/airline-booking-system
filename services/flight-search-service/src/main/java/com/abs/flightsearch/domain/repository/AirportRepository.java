package com.abs.flightsearch.domain.repository;

import com.abs.flightsearch.domain.aggregate.AirportAggregate;

import java.util.Optional;

public interface AirportRepository {
    AirportAggregate save(AirportAggregate aggregate);
    Optional<AirportAggregate> findById(String iataCode);
}
