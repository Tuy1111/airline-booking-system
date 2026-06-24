package com.abs.payment.api;

import com.abs.payment.application.PaymentService;
import com.abs.payment.application.dto.SePayWebhookPayload;
import com.abs.payment.infrastructure.sepay.SePayProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

/**
 * Endpoint do SePay POST tới khi có giao dịch vào tài khoản công ty.
 * Cấu hình trên dashboard sepay.vn: URL = https://<host>/api/v1/payments/webhooks/sepay
 *                                 Auth = Apikey + same value as app.sepay.api-key
 */
@Slf4j
@RestController
@RequestMapping("/payments/webhooks/sepay")
@RequiredArgsConstructor
public class SePayWebhookController {

    private final PaymentService paymentService;
    private final SePayProperties props;

    @PostMapping
    public ResponseEntity<?> onWebhook(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @RequestBody SePayWebhookPayload payload) {

        if (!authorized(auth)) {
            log.warn("SePay webhook: unauthorized call");
            // Trả 401 → SePay sẽ retry; nếu không muốn retry trả 200 + success=false
            return ResponseEntity.status(401).body(Map.of("success", false, "error", "unauthorized"));
        }

        boolean applied = paymentService.handleSePayWebhook(payload);
        // SePay quy ước: HTTP 200 + {"success": true} = đã xử lý xong, không retry nữa
        return ResponseEntity.ok(Map.of("success", true, "applied", applied));
    }

    private boolean authorized(String auth) {
        if (auth == null || props.getApiKey() == null) return false;
        String expected = "Apikey " + props.getApiKey();
        // constant-time compare
        return MessageDigest.isEqual(
                auth.getBytes(StandardCharsets.UTF_8),
                expected.getBytes(StandardCharsets.UTF_8));
    }
}
