package com.abs.payment.application.port.in;

/**
 * Inbound Port (driving) — use case "đánh hỏng các payment PENDING đã quá hạn".
 *
 * <p>Với thanh toán chuyển khoản (SePay), nếu khách không chuyển tiền trước
 * {@code expires_at} thì payment phải tự chuyển sang FAILED và phát
 * {@code payment.failed} để booking-service nhả ghế (Saga compensation).
 * Được kích hoạt định kỳ bởi một scheduler ở tầng infrastructure.
 */
public interface ExpirePendingPaymentsUseCase {

    /** @return số payment vừa bị đánh hỏng do quá hạn. */
    int expirePendingPayments();
}
