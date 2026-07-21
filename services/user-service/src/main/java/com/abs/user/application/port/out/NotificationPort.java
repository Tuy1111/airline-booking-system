package com.abs.user.application.port.out;

/** Outbound application port for notifying a user about a completed workflow. */
public interface NotificationPort {

    void send(NotificationMessage message);

    record NotificationMessage(Long userId, String title, String content, String type) {
        public NotificationMessage {
            if (userId == null || userId <= 0) {
                throw new IllegalArgumentException("Notification userId must be positive");
            }
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("Notification title is required");
            }
            if (content == null || content.isBlank()) {
                throw new IllegalArgumentException("Notification content is required");
            }
            if (type == null || type.isBlank()) {
                throw new IllegalArgumentException("Notification type is required");
            }
        }
    }
}
