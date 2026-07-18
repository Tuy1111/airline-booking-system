package com.abs.payment.application.usecase;

import com.abs.payment.application.PaymentEventPublisher;
import com.abs.payment.application.dto.PaymentFailedEvent;
import com.abs.payment.application.port.in.ExpirePendingPaymentsUseCase;
import com.abs.payment.domain.aggregate.Payment;
import com.abs.payment.domain.repository.PaymentRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Hiện thực use case "đánh hỏng payment PENDING quá hạn" ({@link ExpirePendingPaymentsUseCase}).
 *
 * <p>Phát {@code payment.failed} qua outbox để booking-service nhả ghế (Saga compensation).
 * Được gọi định kỳ bởi {@code PaymentExpiryScheduler}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExpirePendingPaymentsService implements ExpirePendingPaymentsUseCase {

    private final PaymentRepository paymentRepo;
    private final PaymentEventPublisher events;
    private final MeterRegistry meters;

    @Override
    @Transactional
    public int expirePendingPayments() {
        LocalDateTime now = LocalDateTime.now();
        String reason = "Payment expired - no transfer received";
        var expired = paymentRepo.findExpiredPending(now);
        int count = 0;
        for (Payment payment : expired) {
            // Conditional update PENDING → FAILED. Nếu trong lúc scan có webhook
            // đánh SUCCESS (hoặc instance khác đã xử lý), update trả về false →
            // KHÔNG ghi đè state và KHÔNG publish payment.failed.
            if (!paymentRepo.markExpiredIfPending(payment.getId(), reason, now)) {
                log.debug("Skip expiry for payment {}: no longer PENDING (won by webhook/other scan)",
                        payment.getPaymentCode());
                continue;
            }

            events.publishFailed(new PaymentFailedEvent(
                    payment.getId(), payment.getPaymentCode(),
                    payment.getBookingId(), payment.getUserId(),
                    payment.getTotal().amount(), reason
            ));
            counter("expired").increment();
            log.info("Payment EXPIRED → FAILED: code={} bookingId={}",
                    payment.getPaymentCode(), payment.getBookingId());
            count++;
        }
        return count;
    }

    private Counter counter(String outcome) {
        return Counter.builder("payment.sepay")
                .tag("outcome", outcome)
                .register(meters);
    }
}
