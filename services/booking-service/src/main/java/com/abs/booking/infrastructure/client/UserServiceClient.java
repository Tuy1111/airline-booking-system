package com.abs.booking.infrastructure.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final UserFeignClient userFeignClient;

    public String getUserEmail(Long userId) {
        try {
            Map<String, Object> userInfo = userFeignClient.getUserById(userId);
            if (userInfo != null && userInfo.containsKey("email")) {
                return String.valueOf(userInfo.get("email"));
            }
        } catch (Exception e) {
            log.warn("Failed to fetch email for userId={} from user-service: {}", userId, e.getMessage());
        }
        return "user" + userId + "@example.com";
    }
}
