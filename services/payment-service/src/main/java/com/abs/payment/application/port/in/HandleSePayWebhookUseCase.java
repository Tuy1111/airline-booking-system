package com.abs.payment.application.port.in;

import com.abs.payment.api.dto.SePayWebhookPayload;

/**
 * Inbound Port (driving) — use case "xử lý webhook từ SePay".
 *
 * <p>Adapter inbound ({@code SePayWebhookController}) gọi vào lõi qua interface này.
 */
public interface HandleSePayWebhookUseCase {

    /**
     * Xử lý 1 webhook event từ SePay.
     *
     * @return {@code true} nếu đã apply (state đổi), {@code false} nếu bỏ qua
     *         (duplicate / unknown / outbound).
     */
    boolean handleSePayWebhook(SePayWebhookPayload payload);
}
