package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, String> {
}
