package com.abs.notification.domain.vo;

import lombok.*;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class NotificationTemplateId implements Serializable {
    private String code;
    private String locale;
    private Channel channel;
}
