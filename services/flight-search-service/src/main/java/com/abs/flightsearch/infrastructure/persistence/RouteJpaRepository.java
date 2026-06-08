package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.infrastructure.persistence.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteJpaRepository extends JpaRepository<RouteEntity, Long> {
    Optional<RouteEntity> findByFromAirport_IataCodeAndToAirport_IataCode(String from, String to);
}
