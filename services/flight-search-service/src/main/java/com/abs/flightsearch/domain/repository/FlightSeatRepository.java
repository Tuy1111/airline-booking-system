package com.abs.flightsearch.domain.repository;

import com.abs.flightsearch.domain.aggregate.FlightSeatAggregate;
import com.abs.flightsearch.domain.vo.FlightSeatId;

import java.util.List;
import java.util.Optional;

public interface FlightSeatRepository {
    FlightSeatAggregate save(FlightSeatAggregate aggregate);
    Optional<FlightSeatAggregate> findById(FlightSeatId id);
    List<FlightSeatAggregate> findByFlightId(Long flightId);
}
