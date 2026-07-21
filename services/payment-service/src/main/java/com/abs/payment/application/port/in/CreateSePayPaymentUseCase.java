package com.abs.payment.application.port.in;

import com.abs.payment.api.dto.CreatePaymentRequest;
import com.abs.payment.domain.aggregate.Payment;

/**
 * Inbound Port (driving) — use case "tạo thanh toán SePay".
 *
 * <p>Là hợp đồng mà adapter inbound (REST controller) gọi vào lõi ứng dụng.
 * Controller phụ thuộc interface này, KHÔNG biết class hiện thực nào đứng sau.
 */
public interface CreateSePayPaymentUseCase {

    /** Tạo Payment mới (idempotent theo idempotencyKey). */
    Payment createSePayPayment(CreatePaymentRequest req, Long userId);

    /** Dựng URL ảnh QR cho payment để hiển thị cho người dùng. */
    String buildQrUrl(Payment payment);
}
