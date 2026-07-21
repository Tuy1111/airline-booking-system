package com.abs.payment.api.controller;

import com.abs.payment.api.dto.SePayWebhookPayload;
import com.abs.payment.application.port.in.HandleSePayWebhookUseCase;
import com.abs.payment.infrastructure.sepay.SePayProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Map;

/**
 * Endpoint do SePay POST tới khi có giao dịch vào tài khoản công ty.
 * Cấu hình trên dashboard sepay.vn: URL = https://<host>/api/v1/payments/webhooks/sepay
 *                                 Auth = Apikey + same value as app.sepay.api-key
 */
@Slf4j
@RestController
@RequestMapping(ApiPath.WEBHOOK_SEPAY)
@RequiredArgsConstructor
public class SePayWebhookController {

    private final HandleSePayWebhookUseCase paymentService;
    private final SePayProperties props;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> onWebhook(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @RequestHeader(value = "X-SePay-Signature", required = false) String signature,
            @RequestHeader(value = "X-SePay-Timestamp", required = false) String timestamp,
            @RequestBody byte[] rawBody) {

        if (!authorized(auth, signature, timestamp, rawBody)) {
            log.warn("SePay webhook: unauthorized or invalid HMAC signature");
            // Trả 401 → SePay sẽ retry; nếu không muốn retry trả 200 + success=false
            return ResponseEntity.status(401).body(Map.of("success", false, "error", "unauthorized"));
        }

        final SePayWebhookPayload payload;
        try {
            payload = objectMapper.readValue(rawBody, SePayWebhookPayload.class);
        } catch (IOException ex) {
            log.warn("SePay webhook: invalid JSON payload", ex);
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "invalid_json"));
        }

        boolean applied = paymentService.handleSePayWebhook(payload);
        // SePay quy ước: HTTP 200 + {"success": true} = đã xử lý xong, không retry nữa
        return ResponseEntity.ok(Map.of("success", true, "applied", applied));
    }

    private boolean authorized(String auth, String signature, String timestamp, byte[] rawBody) {
        if (props.getWebhookSecret() != null && !props.getWebhookSecret().isBlank()) {
            return validHmac(signature, timestamp, rawBody);
        }

        if (auth == null || props.getApiKey() == null) return false;
        String expected = "Apikey " + props.getApiKey();
        // constant-time compare
        return MessageDigest.isEqual(
                auth.getBytes(StandardCharsets.UTF_8),
                expected.getBytes(StandardCharsets.UTF_8));
    }

    private boolean validHmac(String signature, String timestamp, byte[] rawBody) {
        if (signature == null || timestamp == null || rawBody == null) return false;

        final long timestampSeconds;
        try {
            timestampSeconds = Long.parseLong(timestamp);
        } catch (NumberFormatException ex) {
            return false;
        }

        if (Math.abs(Instant.now().getEpochSecond() - timestampSeconds)
                > props.getWebhookTimestampToleranceSeconds()) {
            log.warn("SePay webhook: timestamp outside tolerance");
            return false;
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(props.getWebhookSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            mac.update((timestamp + ".").getBytes(StandardCharsets.UTF_8));
            byte[] digest = mac.doFinal(rawBody);
            String expected = "sha256=" + toHex(digest);
            return MessageDigest.isEqual(
                    expected.getBytes(StandardCharsets.US_ASCII),
                    signature.getBytes(StandardCharsets.US_ASCII));
        } catch (GeneralSecurityException ex) {
            log.error("SePay webhook: cannot calculate HMAC", ex);
            return false;
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) hex.append(String.format("%02x", b));
        return hex.toString();
    }
}
