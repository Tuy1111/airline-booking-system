package com.abs.notification.domain.aggregate;

import com.abs.notification.domain.vo.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateAggregate {
    private String code;
    private String locale;
    private Channel channel;
    private String subject;
    private String body;
}
