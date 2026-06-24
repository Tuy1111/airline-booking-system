package com.abs.notification.infrastructure.messaging;

import com.abs.notification.application.NotificationService;
import com.abs.notification.application.dto.SendSmsCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsCommandListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${app.notification.rabbit.sms-queue}")
    public void onSms(SendSmsCommand cmd) {
        log.info("Rx cmd.sms.send: tmpl={} to={}", cmd.templateCode(), cmd.recipient());
        notificationService.sendSms(cmd);
    }
}
