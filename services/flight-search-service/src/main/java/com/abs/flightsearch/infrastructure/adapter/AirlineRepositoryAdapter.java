package com.abs.flightsearch.infrastructure.adapter;

import com.abs.flightsearch.domain.aggregate.AirlineAggregate;
import com.abs.flightsearch.domain.repository.AirlineRepository;
import com.abs.flightsearch.infrastructure.persistence.AirlineJpaRepository;
import com.abs.flightsearch.infrastructure.persistence.mapper.AirlinePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AirlineRepositoryAdapter implements AirlineRepository {
    private final AirlineJpaRepository repository;

    @Override
    public AirlineAggregate save(AirlineAggregate aggregate) {
        return AirlinePersistenceMapper.toAggregate(
                repository.save(AirlinePersistenceMapper.toEntity(aggregate)));
    }

    @Override
    public Optional<AirlineAggregate> findById(String code) {
        return repository.findById(code).map(AirlinePersistenceMapper::toAggregate);
    }

    @Override
    public List<AirlineAggregate> findAll() {
        return repository.findAll().stream()
                .map(AirlinePersistenceMapper::toAggregate)
                .toList();
    }
}
