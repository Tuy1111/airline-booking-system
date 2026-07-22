package com.abs.payment.application.usecase;

import com.abs.payment.api.dto.CreatePaymentRequest;
import com.abs.payment.application.SePayQrService;
import com.abs.payment.application.port.in.CreateSePayPaymentUseCase;
import com.abs.payment.domain.aggregate.Payment;
import com.abs.payment.domain.enums.PaymentGateway;
import com.abs.payment.domain.enums.PaymentMethod;
import com.abs.payment.domain.enums.PaymentStatus;
import com.abs.payment.domain.repository.PaymentRepository;
import com.abs.payment.domain.vo.Money;
import com.abs.payment.infrastructure.sepay.SePayProperties;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Hiện thực use case "tạo thanh toán SePay" ({@link CreateSePayPaymentUseCase}).
 *
 * <p>Idempotent theo {@code idempotencyKey} — gọi lại với cùng key trả về payment đã tạo.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateSePayPaymentService implements CreateSePayPaymentUseCase {

    private final PaymentRepository paymentRepo;
    private final SePayQrService qrService;
    private final SePayProperties sepayProps;
    private final MeterRegistry meters;

    @Override
    @Transactional
    public Payment createSePayPayment(CreatePaymentRequest req, Long userId) {
        // Idempotency
        var existing = paymentRepo.findByIdempotencyKey(req.idempotencyKey());
        if (existing.isPresent()) return existing.get();

        String paymentCode  = "PAY" + RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        String transferCode = qrService.newTransferCode(req.bookingId());

        Payment p = Payment.builder()
                .paymentCode(paymentCode)
                .bookingId(req.bookingId())
                .userId(userId)
                .total(Money.vnd(req.amount()))
                .method(req.method() == null ? PaymentMethod.BANK_TRANSFER : req.method())
                .gateway(PaymentGateway.SEPAY)
                .status(PaymentStatus.PENDING)
                .idempotencyKey(req.idempotencyKey())
                .transferCode(transferCode)
                .expiresAt(LocalDateTime.now().plusMinutes(sepayProps.getExpiresMinutes()))
                .build();
        p = paymentRepo.save(p);

        counter("created").increment();
        log.info("Payment created: code={} amount={} transferCode={}",
                p.getPaymentCode(), p.getTotal().amount(), p.getTransferCode());
        return p;
    }

    @Override
    public String buildQrUrl(Payment p) {
        return qrService.buildQrUrl(p.getTransferCode(), p.getTotal().amount());
    }

    private Counter counter(String outcome) {
        return Counter.builder("payment.sepay")
                .tag("outcome", outcome)
                .register(meters);
    }
}
