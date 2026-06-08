package com.abs.user.infrastructure.persistence;

import com.abs.user.infrastructure.persistence.entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerJpaRepository extends JpaRepository<PassengerEntity, Long> {
}
