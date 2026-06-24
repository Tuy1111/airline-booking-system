package com.abs.flightsearch.infrastructure.adapter;

import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;
import com.abs.flightsearch.domain.repository.SeatInventoryRepository;
import com.abs.flightsearch.infrastructure.persistence.FlightJpaRepository;
import com.abs.flightsearch.infrastructure.persistence.SeatInventoryJpaRepository;
import com.abs.flightsearch.infrastructure.persistence.entity.SeatInventoryEntity;
import com.abs.flightsearch.infrastructure.persistence.mapper.SeatInventoryPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeatInventoryRepositoryAdapter implements SeatInventoryRepository {
    private final SeatInventoryJpaRepository repository;
    private final FlightJpaRepository flightRepository;

    @Override
    public SeatInventoryAggregate save(SeatInventoryAggregate aggregate) {
        SeatInventoryEntity entity = SeatInventoryPersistenceMapper.toEntity(aggregate);
        entity.setFlight(flightRepository.getReferenceById(aggregate.getFlightId()));
        return SeatInventoryPersistenceMapper.toAggregate(repository.save(entity));
    }

    @Override
    public Optional<SeatInventoryAggregate> findById(Long flightId) {
        return repository.findById(flightId).map(SeatInventoryPersistenceMapper::toAggregate);
    }
}
