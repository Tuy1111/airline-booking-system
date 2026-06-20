package com.abs.payment.infrastructure.adapter;

import com.abs.payment.domain.aggregate.Payment;
import com.abs.payment.domain.enums.PaymentStatus;
import com.abs.payment.domain.repository.PaymentRepository;
import com.abs.payment.infrastructure.persistence.PaymentJpaRepository;
import com.abs.payment.infrastructure.persistence.mapper.PaymentPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class PaymentRepositoryAdapter implements PaymentRepository {
    private final PaymentJpaRepository repository;

    @Override
    public Payment save(Payment aggregate) {
        return PaymentPersistenceMapper.toAggregate(
                repository.save(PaymentPersistenceMapper.toEntity(aggregate)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findById(Long id) {
        return repository.findById(id).map(PaymentPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findByPaymentCode(String paymentCode) {
        return repository.findByPaymentCode(paymentCode).map(PaymentPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey)
                .map(PaymentPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findByTransferCode(String transferCode) {
        return repository.findByTransferCode(transferCode).map(PaymentPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(PaymentPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByBookingId(Long bookingId) {
        return repository.findByBookingId(bookingId).stream()
                .map(PaymentPersistenceMapper::toAggregate)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findExpiredPending(LocalDateTime now) {
        return repository.findByStatusAndExpiresAtBefore(PaymentStatus.PENDING, now).stream()
                .map(PaymentPersistenceMapper::toAggregate)
                .toList();
    }

    @Override
    public boolean markExpiredIfPending(Long id, String reason, LocalDateTime now) {
        return repository.markExpiredIfPending(id, reason, now) == 1;
    }
}
