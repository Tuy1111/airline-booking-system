package com.abs.booking.infrastructure.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${app.services.user.url:http://localhost:8083}")
    private String baseUrl;

    @SuppressWarnings("unchecked")
    public String getUserEmail(Long userId) {
        try {
            String url = baseUrl + "/api/v1/users/" + userId;
            Map<String, Object> userInfo = restTemplate.getForObject(url, Map.class);
            if (userInfo != null && userInfo.containsKey("email")) {
                return String.valueOf(userInfo.get("email"));
            }
        } catch (Exception e) {
            log.warn("Failed to fetch email for userId={} from user-service: {}", userId, e.getMessage());
        }
        return "user" + userId + "@example.com";
    }
}
