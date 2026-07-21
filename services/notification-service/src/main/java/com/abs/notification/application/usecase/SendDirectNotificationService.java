package com.abs.notification.application.usecase;

import com.abs.notification.domain.aggregate.Notification;
import com.abs.notification.domain.enums.Channel;
import com.abs.notification.domain.enums.NotificationStatus;
import com.abs.notification.domain.repository.NotificationRepository;
import com.abs.notification.domain.vo.Recipient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

/** Records a direct in-app notification received over HTTP. */
@Service
@RequiredArgsConstructor
@Slf4j
public class SendDirectNotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification send(Long userId, String title, String content, String notificationType) {
        return send(userId, title, content, notificationType, null);
    }

    @Transactional
    public Notification send(Long userId, String title, String content, String notificationType, String dedupKey) {
        String type = notificationType.trim().toUpperCase(Locale.ROOT);
        if (dedupKey != null) {
            var existing = notificationRepository.findByDedupKey(dedupKey);
            if (existing.isPresent()) return existing.get();
        }

        Notification notification = Notification.builder()
                .templateCode(type)
                .userId(userId)
                .recipient(Recipient.of(Channel.PUSH, "user:" + userId))
                .variables(Map.of(
                        "title", title.trim(),
                        "content", content,
                        "type", type))
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .dedupKey(dedupKey)
                .build();

        // Direct notifications are accepted synchronously; PUSH delivery is stubbed by logging in dev.
        log.info("[PUSH-STUB] userId={} title={} content={}",
                userId, title, content);
        notification.markSent(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
}
