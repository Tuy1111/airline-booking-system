package com.abs.payment.infrastructure.persistence;

import com.abs.payment.domain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}
