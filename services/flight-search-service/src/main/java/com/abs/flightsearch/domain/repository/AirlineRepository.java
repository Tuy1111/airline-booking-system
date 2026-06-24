package com.abs.flightsearch.domain.repository;

import com.abs.flightsearch.domain.aggregate.AirlineAggregate;

import java.util.Optional;

public interface AirlineRepository {
    AirlineAggregate save(AirlineAggregate aggregate);
    Optional<AirlineAggregate> findById(String code);
}
