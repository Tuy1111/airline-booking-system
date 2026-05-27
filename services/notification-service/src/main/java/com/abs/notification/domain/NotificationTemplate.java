package com.abs.notification.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_template")
@IdClass(NotificationTemplateId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationTemplate {

    @Id
    @Column(length = 50)
    private String code;

    @Id
    @Column(length = 5)
    private String locale;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Channel channel;

    @Column(length = 255)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;
}
