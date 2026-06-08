package com.abs.flightsearch.domain.repository;

import com.abs.flightsearch.domain.aggregate.RouteAggregate;

import java.util.Optional;

public interface RouteRepository {
    RouteAggregate save(RouteAggregate aggregate);
    Optional<RouteAggregate> findById(Long id);
    Optional<RouteAggregate> findByAirports(String from, String to);
}
