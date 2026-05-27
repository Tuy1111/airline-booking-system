package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.domain.Flight;
import com.abs.flightsearch.domain.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNo(String flightNo);

    @Query("""
            SELECT f FROM Flight f
            JOIN f.route r
            WHERE r.fromAirport.iataCode = :from
              AND r.toAirport.iataCode   = :to
              AND f.departureTime BETWEEN :start AND :end
              AND f.status = :status
            ORDER BY f.departureTime
            """)
    List<Flight> searchFlights(@Param("from") String from,
                               @Param("to") String to,
                               @Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end,
                               @Param("status") FlightStatus status);
}
