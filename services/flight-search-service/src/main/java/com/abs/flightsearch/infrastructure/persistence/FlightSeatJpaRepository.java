package com.abs.flightsearch.infrastructure.persistence;

import com.abs.flightsearch.domain.vo.FlightSeatId;
import com.abs.flightsearch.infrastructure.persistence.entity.FlightSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightSeatJpaRepository extends JpaRepository<FlightSeatEntity, FlightSeatId> {
    List<FlightSeatEntity> findByFlightId(Long flightId);
}
