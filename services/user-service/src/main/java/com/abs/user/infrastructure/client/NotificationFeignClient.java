package com.abs.user.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", path = "/api/v1/notifications")
public interface NotificationFeignClient {

    @PostMapping("/send")
    void send(@RequestBody SendNotificationRequest request);

    record SendNotificationRequest(Long userId, String title, String content, String type) {
    }
}
