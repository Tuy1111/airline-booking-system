package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.infrastructure.persistence.entity.SeatInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatInventoryJpaRepository extends JpaRepository<SeatInventoryEntity, Long> {
}
