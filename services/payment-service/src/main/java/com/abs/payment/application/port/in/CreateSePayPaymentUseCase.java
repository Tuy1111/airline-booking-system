package com.abs.payment.application.port.in;

import com.abs.payment.application.dto.CreatePaymentRequest;
import com.abs.payment.domain.aggregate.PaymentAggregate;

/**
 * Inbound Port (driving) — use case "tạo thanh toán SePay".
 *
 * <p>Là hợp đồng mà adapter inbound (REST controller) gọi vào lõi ứng dụng.
 * Controller phụ thuộc interface này, KHÔNG biết class hiện thực nào đứng sau.
 */
public interface CreateSePayPaymentUseCase {

    /** Tạo Payment mới (idempotent theo idempotencyKey). */
    PaymentAggregate createSePayPayment(CreatePaymentRequest req);

    /** Dựng URL ảnh QR cho payment để hiển thị cho người dùng. */
    String buildQrUrl(PaymentAggregate payment);
}
