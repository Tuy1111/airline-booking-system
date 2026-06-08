package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.infrastructure.persistence.entity.AirlineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineJpaRepository extends JpaRepository<AirlineEntity, String> {
}
