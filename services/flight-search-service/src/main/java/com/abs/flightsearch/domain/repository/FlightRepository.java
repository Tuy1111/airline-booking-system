package com.abs.flightsearch.domain.repository;

import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.vo.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository {
    FlightAggregate save(FlightAggregate aggregate);
    Optional<FlightAggregate> findById(Long id);
    boolean existsById(Long id);
    Optional<FlightAggregate> findByFlightNo(String flightNo);
    List<FlightAggregate> searchFlights(
            String from,
            String to,
            LocalDateTime start,
            LocalDateTime end,
            FlightStatus status);
}
