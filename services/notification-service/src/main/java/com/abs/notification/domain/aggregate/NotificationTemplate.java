package com.abs.notification.domain.aggregate;

import com.abs.notification.domain.enums.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplate {
    private String code;
    private String locale;
    private Channel channel;
    private String subject;
    private String body;
}
