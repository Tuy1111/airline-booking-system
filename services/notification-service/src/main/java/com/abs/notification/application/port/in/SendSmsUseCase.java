package com.abs.notification.application.port.in;

import com.abs.notification.application.dto.SendSmsCommand;
import com.abs.notification.domain.aggregate.NotificationAggregate;

/**
 * Inbound Port (driving) — use case "gửi SMS".
 *
 * <p>Adapter inbound ({@code SmsCommandListener}) gọi vào lõi qua interface này.
 */
public interface SendSmsUseCase {

    /** Render template + gửi SMS, lưu lại bản ghi Notification. */
    NotificationAggregate sendSms(SendSmsCommand cmd);
}
