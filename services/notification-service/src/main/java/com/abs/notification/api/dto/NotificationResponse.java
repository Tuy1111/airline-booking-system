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
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime sentAt,
        LocalDateTime readAt
) {
    public static NotificationResponse of(Notification n) {
        return new NotificationResponse(
                n.getId(), n.getTemplateCode(), n.getUserId(),
                n.getRecipient() == null ? null : n.getRecipient().channel(),
                n.getRecipient() == null ? null : n.getRecipient().address(),
                n.getStatus(), n.getRetryCount(), n.getErrorMessage(),
                displayTitle(n), displayContent(n),
                n.getCreatedAt(), n.getSentAt(), n.getReadAt()
        );
    }

    private static String displayTitle(Notification notification) {
        Object title = variable(notification, "title");
        if (title != null) {
            return String.valueOf(title);
        }
        return switch (notification.getTemplateCode()) {
            case "BOOKING_CONFIRMED" -> "Đặt vé thành công";
            case "BOOKING_CANCELLED" -> "Đặt vé đã bị hủy";
            case "KYC_VERIFIED" -> "Xác minh hộ chiếu thành công";
            case "KYC_REJECTED" -> "Xác minh hộ chiếu không thành công";
            default -> "Thông báo hệ thống";
        };
    }

    private static String displayContent(Notification notification) {
        Object content = variable(notification, "content");
        if (content != null) {
            return String.valueOf(content);
        }
        Object bookingCode = variable(notification, "bookingCode");
        return switch (notification.getTemplateCode()) {
            case "BOOKING_CONFIRMED" -> bookingCode == null
                    ? "Vé của bạn đã được xác nhận."
                    : "Đặt chỗ " + bookingCode + " đã được xác nhận.";
            case "BOOKING_CANCELLED" -> bookingCode == null
                    ? "Đặt vé của bạn đã bị hủy."
                    : "Đặt chỗ " + bookingCode + " đã bị hủy.";
            default -> "Bạn có một thông báo mới.";
        };
    }

    private static Object variable(Notification notification, String key) {
        return notification.getVariables() == null ? null : notification.getVariables().get(key);
    }
}
