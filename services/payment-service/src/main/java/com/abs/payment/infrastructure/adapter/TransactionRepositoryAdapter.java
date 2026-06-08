package com.abs.payment.infrastructure.adapter;

import com.abs.payment.domain.aggregate.TransactionAggregate;
import com.abs.payment.domain.repository.TransactionRepository;
import com.abs.payment.infrastructure.persistence.PaymentJpaRepository;
import com.abs.payment.infrastructure.persistence.TransactionJpaRepository;
import com.abs.payment.infrastructure.persistence.mapper.TransactionPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepository {
    private final TransactionJpaRepository repository;
    private final PaymentJpaRepository paymentRepository;

    @Override
    public TransactionAggregate save(TransactionAggregate aggregate) {
        var payment = paymentRepository.getReferenceById(aggregate.getPaymentId());
        return TransactionPersistenceMapper.toAggregate(
                repository.save(TransactionPersistenceMapper.toEntity(aggregate, payment)));
    }
}
