package com.abs.user.infrastructure.adapter;

import com.abs.user.application.port.out.NotificationPort;
import com.abs.user.infrastructure.client.NotificationFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationAdapter implements NotificationPort {

    private final NotificationFeignClient client;

    @Override
    public void send(NotificationMessage message) {
        log.info("Calling notification-service: userId={}, type={}", message.userId(), message.type());
        client.send(new NotificationFeignClient.SendNotificationRequest(
                message.userId(), message.title(), message.content(), message.type()));
    }
}
