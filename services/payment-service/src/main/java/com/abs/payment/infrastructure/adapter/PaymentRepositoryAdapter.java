package com.abs.payment.infrastructure.adapter;

import com.abs.payment.domain.aggregate.PaymentAggregate;
import com.abs.payment.domain.repository.PaymentRepository;
import com.abs.payment.infrastructure.persistence.PaymentJpaRepository;
import com.abs.payment.infrastructure.persistence.mapper.PaymentPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class PaymentRepositoryAdapter implements PaymentRepository {
    private final PaymentJpaRepository repository;

    @Override
    public PaymentAggregate save(PaymentAggregate aggregate) {
        return PaymentPersistenceMapper.toAggregate(
                repository.save(PaymentPersistenceMapper.toEntity(aggregate)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentAggregate> findById(Long id) {
        return repository.findById(id).map(PaymentPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentAggregate> findByPaymentCode(String paymentCode) {
        return repository.findByPaymentCode(paymentCode).map(PaymentPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentAggregate> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey)
                .map(PaymentPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentAggregate> findByTransferCode(String transferCode) {
        return repository.findByTransferCode(transferCode).map(PaymentPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentAggregate> findByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(PaymentPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentAggregate> findByBookingId(Long bookingId) {
        return repository.findByBookingId(bookingId).stream()
                .map(PaymentPersistenceMapper::toAggregate)
                .toList();
    }
}
