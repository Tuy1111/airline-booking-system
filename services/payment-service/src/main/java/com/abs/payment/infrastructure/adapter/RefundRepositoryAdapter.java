package com.abs.payment.infrastructure.adapter;

import com.abs.payment.domain.aggregate.Refund;
import com.abs.payment.domain.repository.RefundRepository;
import com.abs.payment.infrastructure.persistence.PaymentJpaRepository;
import com.abs.payment.infrastructure.persistence.RefundJpaRepository;
import com.abs.payment.infrastructure.persistence.mapper.RefundPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefundRepositoryAdapter implements RefundRepository {
    private final RefundJpaRepository repository;
    private final PaymentJpaRepository paymentRepository;

    @Override
    public Refund save(Refund aggregate) {
        var payment = paymentRepository.getReferenceById(aggregate.getPaymentId());
        return RefundPersistenceMapper.toAggregate(
                repository.save(RefundPersistenceMapper.toEntity(aggregate, payment)));
    }
}
