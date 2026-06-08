package com.abs.user.infrastructure.adapter;

import com.abs.user.domain.aggregate.PassengerAggregate;
import com.abs.user.domain.repository.PassengerRepository;
import com.abs.user.infrastructure.persistence.PassengerJpaRepository;
import com.abs.user.infrastructure.persistence.UserJpaRepository;
import com.abs.user.infrastructure.persistence.mapper.PassengerPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PassengerRepositoryAdapter implements PassengerRepository {
    private final PassengerJpaRepository repository;
    private final UserJpaRepository userRepository;

    @Override
    public PassengerAggregate save(PassengerAggregate aggregate) {
        var user = userRepository.getReferenceById(aggregate.getUserId());
        return PassengerPersistenceMapper.toAggregate(
                repository.save(PassengerPersistenceMapper.toEntity(aggregate, user)));
    }

    @Override
    public Optional<PassengerAggregate> findById(Long userId) {
        return repository.findById(userId).map(PassengerPersistenceMapper::toAggregate);
    }
}
