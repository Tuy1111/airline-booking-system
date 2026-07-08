package com.abs.notification.domain.aggregate;

import com.abs.notification.domain.enums.NotificationStatus;
import com.abs.notification.domain.vo.Recipient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long id;
    private String templateCode;
    private Long userId;
    private Recipient recipient;
    private Map<String, Object> variables;
    private NotificationStatus status;
    private Integer retryCount;
    private String errorMessage;
    private String dedupKey;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    public static Notification queue(String templateCode, Long userId, Recipient recipient, java.util.Map<String,Object> variables) {
        return Notification.builder()
                .templateCode(templateCode).userId(userId).recipient(recipient)
                .variables(variables).status(NotificationStatus.PENDING).retryCount(0)
                .build();
    }

    public void markSent(LocalDateTime sentAt) {
        status = NotificationStatus.SENT;
        this.sentAt = sentAt;
        errorMessage = null;
    }

    public void markFailed(String errorMessage) {
        status = NotificationStatus.FAILED;
        this.errorMessage = errorMessage;
    }
}
