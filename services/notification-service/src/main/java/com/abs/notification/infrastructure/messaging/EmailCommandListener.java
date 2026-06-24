package com.abs.notification.infrastructure.messaging;

import com.abs.notification.application.NotificationService;
import com.abs.notification.application.dto.SendEmailCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailCommandListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${app.notification.rabbit.email-queue}")
    public void onEmail(SendEmailCommand cmd) {
        log.info("Rx cmd.email.send: tmpl={} to={}", cmd.templateCode(), cmd.recipient());
        notificationService.sendEmail(cmd);
    }
}
