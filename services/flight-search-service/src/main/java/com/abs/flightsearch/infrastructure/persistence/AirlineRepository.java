package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.domain.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, String> {
}
