package com.abs.notification.infrastructure.persistence.entity;

import com.abs.notification.domain.enums.Channel;
import com.abs.notification.domain.vo.NotificationTemplateId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_template")
@IdClass(NotificationTemplateId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationTemplateEntity {

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
