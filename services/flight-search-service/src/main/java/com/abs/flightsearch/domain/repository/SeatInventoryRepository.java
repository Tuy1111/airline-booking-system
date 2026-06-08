package com.abs.flightsearch.domain.repository;

import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;

import java.util.Optional;

public interface SeatInventoryRepository {
    SeatInventoryAggregate save(SeatInventoryAggregate aggregate);
    Optional<SeatInventoryAggregate> findById(Long flightId);
}
