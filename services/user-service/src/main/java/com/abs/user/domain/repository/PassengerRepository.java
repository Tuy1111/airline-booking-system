package com.abs.user.domain.repository;

import com.abs.user.domain.aggregate.PassengerAggregate;

import java.util.Optional;

public interface PassengerRepository {
    PassengerAggregate save(PassengerAggregate aggregate);
    Optional<PassengerAggregate> findById(Long userId);
}
