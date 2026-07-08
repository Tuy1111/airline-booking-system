package com.abs.notification.application.dto;

import java.util.Map;

/**
 * @param dedupKey khoá chống trùng (nullable). Khi khác null, một event đã xử lý
 *                 trước đó sẽ được bỏ qua thay vì gửi email lần nữa (idempotency
 *                 cho consumer Kafka/Rabbit). Vd: "BOOKING_CONFIRMED:ABS123".
 */
public record SendEmailCommand(
        String templateCode,
        String locale,
        Long userId,
        String recipient,
        Map<String, Object> variables,
        String dedupKey
) {}
