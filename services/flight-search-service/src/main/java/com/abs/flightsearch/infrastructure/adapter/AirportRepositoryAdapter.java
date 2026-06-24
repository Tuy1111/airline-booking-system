package com.abs.flightsearch.infrastructure.adapter;

import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import com.abs.flightsearch.domain.repository.AirportRepository;
import com.abs.flightsearch.infrastructure.persistence.AirportJpaRepository;
import com.abs.flightsearch.infrastructure.persistence.mapper.AirportPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AirportRepositoryAdapter implements AirportRepository {
    private final AirportJpaRepository repository;

    @Override
    public AirportAggregate save(AirportAggregate aggregate) {
        return AirportPersistenceMapper.toAggregate(
                repository.save(AirportPersistenceMapper.toEntity(aggregate)));
    }

    @Override
    public Optional<AirportAggregate> findById(String iataCode) {
        return repository.findById(iataCode).map(AirportPersistenceMapper::toAggregate);
    }
}
