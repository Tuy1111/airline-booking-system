package com.abs.notification.domain.aggregate;

import com.abs.notification.domain.vo.Channel;
import com.abs.notification.domain.vo.NotificationStatus;
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
public class NotificationAggregate {
    private Long id;
    private String templateCode;
    private Long userId;
    private Channel channel;
    private String recipient;
    private Map<String, Object> variables;
    private NotificationStatus status;
    private Integer retryCount;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

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
