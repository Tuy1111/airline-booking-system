package com.abs.user.infrastructure.adapter;

import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.infrastructure.persistence.UserJpaRepository;
import com.abs.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository repository;

    @Override
    public UserAggregate save(UserAggregate aggregate) {
        return UserPersistenceMapper.toAggregate(
                repository.save(UserPersistenceMapper.toEntity(aggregate)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAggregate> findById(Long id) {
        return repository.findById(id).map(UserPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAggregate> findByEmail(String email) {
        return repository.findByEmail(email).map(UserPersistenceMapper::toAggregate);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
