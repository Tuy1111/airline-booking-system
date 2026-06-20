package com.abs.notification.api.dto;

import com.abs.notification.domain.aggregate.Notification;
import com.abs.notification.domain.enums.Channel;
import com.abs.notification.domain.enums.NotificationStatus;

import java.time.LocalDateTime;

/**
 * DTO trả ra ngoài HTTP — không expose trực tiếp domain aggregate {@link Notification}
 * (tránh rò rỉ chi tiết nội bộ như {@code variables}).
 */
public record NotificationResponse(
        Long id,
        String templateCode,
        Long userId,
        Channel channel,
        String recipient,
        NotificationStatus status,
        Integer retryCount,
        String errorMessage,
        LocalDateTime createdAt,
        LocalDateTime sentAt
) {
    public static NotificationResponse of(Notification n) {
        return new NotificationResponse(
                n.getId(), n.getTemplateCode(), n.getUserId(),
                n.getRecipient() == null ? null : n.getRecipient().channel(),
                n.getRecipient() == null ? null : n.getRecipient().address(),
                n.getStatus(), n.getRetryCount(), n.getErrorMessage(),
                n.getCreatedAt(), n.getSentAt()
        );
    }
}
