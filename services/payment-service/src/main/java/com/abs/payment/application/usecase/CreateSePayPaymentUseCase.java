package com.abs.payment.application.usecase;

import com.abs.payment.application.SePayQrService;
import com.abs.payment.application.dto.CreatePaymentRequest;
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
 * Use case "tạo thanh toán SePay".
 *
 * <p>Idempotent theo {@code idempotencyKey} — gọi lại với cùng key trả về payment đã tạo.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateSePayPaymentUseCase {

    private final PaymentRepository paymentRepo;
    private final SePayQrService qrService;
    private final SePayProperties sepayProps;
    private final MeterRegistry meters;

    /**
     * Tạo Payment mới gắn với SePay. Idempotent theo idempotencyKey —
     * gọi lại với cùng key trả về payment đã tạo.
     */
    @Transactional
    public Payment createSePayPayment(CreatePaymentRequest req) {
        // Idempotency
        var existing = paymentRepo.findByIdempotencyKey(req.idempotencyKey());
        if (existing.isPresent()) return existing.get();

        String paymentCode  = "PAY" + RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        String transferCode = qrService.newTransferCode(req.bookingId());

        Payment p = Payment.builder()
                .paymentCode(paymentCode)
                .bookingId(req.bookingId())
                .userId(req.userId())
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

    /** Dựng URL ảnh QR cho payment để hiển thị cho người dùng. */
    public String buildQrUrl(Payment p) {
        return qrService.buildQrUrl(p.getTransferCode(), p.getTotal().amount());
    }

    private Counter counter(String outcome) {
        return Counter.builder("payment.sepay")
                .tag("outcome", outcome)
                .register(meters);
    }
}
