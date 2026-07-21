package com.abs.notification.domain.vo;

import com.abs.notification.domain.enums.Channel;
import lombok.*;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class NotificationTemplateId implements Serializable {
    private String code;
    private String locale;
    private Channel channel;
}
