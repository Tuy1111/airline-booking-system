package com.abs.payment.infrastructure.persistence;

import com.abs.payment.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
