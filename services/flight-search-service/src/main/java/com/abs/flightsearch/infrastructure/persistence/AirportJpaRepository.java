package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.infrastructure.persistence.entity.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportJpaRepository extends JpaRepository<AirportEntity, String> {
}
