package com.abs.payment.infrastructure.scheduling;

import com.abs.payment.application.port.in.ExpirePendingPaymentsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Driving adapter: định kỳ kích hoạt use case đánh hỏng payment quá hạn.
 * Tách scheduler khỏi business logic để giữ lõi (application) độc lập với Spring scheduling.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentExpiryScheduler {

    private final ExpirePendingPaymentsUseCase expirePendingPayments;

    @Scheduled(fixedDelayString = "${app.payment.expiry-scan-ms:60000}")
    public void scan() {
        try {
            expirePendingPayments.expirePendingPayments();
        } catch (Exception ex) {
            // Không để 1 lần quét lỗi làm chết scheduler — lần sau quét lại.
            log.error("Payment expiry scan failed: {}", ex.getMessage(), ex);
        }
    }
}
