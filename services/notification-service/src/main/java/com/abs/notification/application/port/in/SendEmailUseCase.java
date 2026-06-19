package com.abs.notification.application.port.in;

import com.abs.notification.application.dto.SendEmailCommand;
import com.abs.notification.domain.aggregate.NotificationAggregate;

/**
 * Inbound Port (driving) — use case "gửi email".
 *
 * <p>Các adapter inbound (REST controller, Kafka/RabbitMQ listener) gọi vào lõi
 * qua interface này, KHÔNG phụ thuộc class hiện thực.
 */
public interface SendEmailUseCase {

    /** Render template + gửi email, lưu lại bản ghi Notification. */
    NotificationAggregate sendEmail(SendEmailCommand cmd);
}
