package com.abs.notification.application.dto;

import java.util.Map;

public record SendEmailCommand(
        String templateCode,
        String locale,
        Long userId,
        String recipient,
        Map<String, Object> variables
) {}
