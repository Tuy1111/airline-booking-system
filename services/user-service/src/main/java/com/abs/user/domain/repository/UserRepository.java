package com.abs.user.domain.repository;

import com.abs.user.domain.aggregate.UserAggregate;

import java.util.Optional;

public interface UserRepository {
    UserAggregate save(UserAggregate aggregate);
    Optional<UserAggregate> findById(Long id);
    Optional<UserAggregate> findByEmail(String email);
    boolean existsByEmail(String email);
}
