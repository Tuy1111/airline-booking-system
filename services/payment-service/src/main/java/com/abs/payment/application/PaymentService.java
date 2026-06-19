package com.abs.payment.application;

import com.abs.payment.application.dto.CreatePaymentRequest;
import com.abs.payment.application.dto.PaymentCompletedEvent;
import com.abs.payment.application.dto.PaymentFailedEvent;
import com.abs.payment.application.dto.SePayWebhookPayload;
import com.abs.payment.application.port.in.CreateSePayPaymentUseCase;
import com.abs.payment.application.port.in.HandleSePayWebhookUseCase;
import com.abs.payment.domain.aggregate.Payment;
import com.abs.payment.domain.aggregate.Transaction;
import com.abs.payment.domain.repository.PaymentRepository;
import com.abs.payment.domain.repository.TransactionRepository;
import com.abs.payment.domain.vo.Money;
import com.abs.payment.domain.enums.PaymentGateway;
import com.abs.payment.domain.enums.PaymentMethod;
import com.abs.payment.domain.enums.PaymentStatus;
import com.abs.payment.infrastructure.sepay.SePayProperties;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService implements CreateSePayPaymentUseCase, HandleSePayWebhookUseCase {

    private static final Pattern CODE_IN_CONTENT =
            Pattern.compile("(ABS\\w{4,})", Pattern.CASE_INSENSITIVE);

    private final PaymentRepository paymentRepo;
    private final TransactionRepository txnRepo;
    private final SePayQrService qrService;
    private final SePayProperties sepayProps;
    private final PaymentEventPublisher events;
    private final MeterRegistry meters;

    /**
     * Tạo Payment mới gắn với SePay. Idempotent theo idempotencyKey —
     * gọi lại với cùng key trả về payment đã tạo.
     */
    @Override
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

    @Override
    public String buildQrUrl(Payment p) {
        return qrService.buildQrUrl(p.getTransferCode(), p.getTotal().amount());
    }

    /**
     * Xử lý 1 webhook event từ SePay. Trả về true nếu đã apply (state thay đổi),
     * false nếu ignored (duplicate / unknown / outbound).
     */
    @Override
    @Transactional
    public boolean handleSePayWebhook(SePayWebhookPayload p) {
        if (p == null || !"in".equalsIgnoreCase(p.transferType())) {
            log.debug("SePay webhook ignored (non-inbound): {}", p);
            return false;
        }

        String code = p.code() != null && !p.code().isBlank()
                ? p.code()
                : extractCodeFromContent(p.content());
        if (code == null) {
            log.warn("SePay webhook: no transfer code in content='{}'", p.content());
            counter("webhook.no_code").increment();
            return false;
        }

        Payment payment = paymentRepo.findByTransferCode(code).orElse(null);
        if (payment == null) {
            log.warn("SePay webhook: unknown transferCode={}", code);
            counter("webhook.unknown_code").increment();
            return false;
        }

        // Idempotency: SePay đôi khi retry
        if (p.referenceCode() != null
                && p.referenceCode().equals(payment.getReferenceCode())) {
            log.info("SePay webhook duplicate ref={} payment={}",
                    p.referenceCode(), payment.getPaymentCode());
            counter("webhook.duplicate").increment();
            return false;
        }

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            log.info("SePay webhook: payment {} already SUCCESS, ignoring", payment.getPaymentCode());
            return false;
        }

        // Đối soát số tiền
        BigDecimal got      = p.transferAmount();
        BigDecimal expected = payment.getTotal().amount();
        if (got == null || expected.compareTo(got) != 0) {
            return markFailed(payment, p,
                    "amount mismatch: expected=" + expected + " got=" + got);
        }

        // OK → mark SUCCESS
        payment.markSuccessful(p.referenceCode(), LocalDateTime.now());
        payment = paymentRepo.save(payment);

        Transaction txn = Transaction.builder()
                .paymentId(payment.getId())
                .gatewayTxnId(p.referenceCode())
                .gatewayResponse(truncate(p.toString(), 4000))
                .status("SUCCESS")
                .build();
        txnRepo.save(txn);

        events.publishCompleted(new PaymentCompletedEvent(
                payment.getId(), payment.getPaymentCode(),
                payment.getBookingId(), payment.getUserId(),
                payment.getTotal().amount(), payment.getTotal().currency(),
                payment.getGateway().name(), payment.getReferenceCode(),
                payment.getCompletedAt()
        ));

        counter("webhook.success").increment();
        log.info("Payment SUCCESS: code={} ref={}",
                payment.getPaymentCode(), payment.getReferenceCode());
        return true;
    }

    private boolean markFailed(Payment payment, SePayWebhookPayload p, String reason) {
        payment.markFailed(reason, p.referenceCode(), LocalDateTime.now());
        payment = paymentRepo.save(payment);

        txnRepo.save(Transaction.builder()
                .paymentId(payment.getId())
                .gatewayTxnId(p.referenceCode())
                .gatewayResponse(truncate(p.toString(), 4000))
                .status("FAILED")
                .build());

        events.publishFailed(new PaymentFailedEvent(
                payment.getId(), payment.getPaymentCode(),
                payment.getBookingId(), payment.getUserId(),
                payment.getTotal().amount(), reason
        ));

        counter("webhook.failed").increment();
        log.warn("Payment FAILED: code={} reason={}", payment.getPaymentCode(), reason);
        return true;
    }

    private static String extractCodeFromContent(String content) {
        if (content == null) return null;
        Matcher m = CODE_IN_CONTENT.matcher(content);
        return m.find() ? m.group(1).toUpperCase() : null;
    }

    private static String truncate(String s, int max) {
        return s == null || s.length() <= max ? s : s.substring(0, max);
    }

    private Counter counter(String outcome) {
        return Counter.builder("payment.sepay")
                .tag("outcome", outcome)
                .register(meters);
    }
}
