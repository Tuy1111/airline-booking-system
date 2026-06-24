package com.abs.flightsearch.infrastructure.adapter;

import com.abs.flightsearch.domain.aggregate.FlightSeatAggregate;
import com.abs.flightsearch.domain.repository.FlightSeatRepository;
import com.abs.flightsearch.domain.vo.FlightSeatId;
import com.abs.flightsearch.infrastructure.persistence.FlightSeatJpaRepository;
import com.abs.flightsearch.infrastructure.persistence.mapper.FlightSeatPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FlightSeatRepositoryAdapter implements FlightSeatRepository {
    private final FlightSeatJpaRepository repository;

    @Override
    public FlightSeatAggregate save(FlightSeatAggregate aggregate) {
        return FlightSeatPersistenceMapper.toAggregate(
                repository.save(FlightSeatPersistenceMapper.toEntity(aggregate)));
    }

    @Override
    public Optional<FlightSeatAggregate> findById(FlightSeatId id) {
        return repository.findById(id).map(FlightSeatPersistenceMapper::toAggregate);
    }

    @Override
    public List<FlightSeatAggregate> findByFlightId(Long flightId) {
        return repository.findByFlightId(flightId).stream()
                .map(FlightSeatPersistenceMapper::toAggregate)
                .toList();
    }
}
