package com.abs.payment.infrastructure.persistence;

import com.abs.payment.infrastructure.persistence.entity.RefundEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundJpaRepository extends JpaRepository<RefundEntity, Long> {
}
