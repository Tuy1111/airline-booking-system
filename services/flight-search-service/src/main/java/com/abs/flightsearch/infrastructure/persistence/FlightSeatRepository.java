package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.domain.FlightSeat;
import com.abs.flightsearch.domain.FlightSeatId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightSeatRepository extends JpaRepository<FlightSeat, FlightSeatId> {
    List<FlightSeat> findByFlightId(Long flightId);
}
