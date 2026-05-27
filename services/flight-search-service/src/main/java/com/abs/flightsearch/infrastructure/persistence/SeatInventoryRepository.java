package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.domain.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Long> {
}
