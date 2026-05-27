package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByFromAirport_IataCodeAndToAirport_IataCode(String from, String to);
}
