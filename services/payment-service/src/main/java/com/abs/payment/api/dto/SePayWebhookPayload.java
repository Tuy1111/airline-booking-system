package com.abs.payment.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Payload SePay POST vào /webhooks/sepay.
 * Tham khảo: https://docs.sepay.vn/tich-hop-webhooks.html
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SePayWebhookPayload(
        Long       id,
        String     gateway,            // "MBBank", "Vietcombank", ...
        String     transactionDate,    // "2026-06-01 17:00:00"
        String     accountNumber,
        String     code,               // SePay đã parse sẵn (có thể null)
        String     content,            // nội dung CK gốc
        String     transferType,       // "in" | "out"
        BigDecimal transferAmount,
        BigDecimal accumulated,
        String     subAccount,
        String     referenceCode,      // mã giao dịch ngân hàng
        String     description
) {}
