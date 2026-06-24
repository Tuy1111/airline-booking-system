package com.abs.notification.application;

import com.abs.notification.application.dto.SendEmailCommand;
import com.abs.notification.application.dto.SendSmsCommand;
import com.abs.notification.domain.aggregate.NotificationAggregate;
import com.abs.notification.domain.aggregate.NotificationTemplateAggregate;
import com.abs.notification.domain.repository.NotificationRepository;
import com.abs.notification.domain.repository.NotificationTemplateRepository;
import com.abs.notification.domain.vo.Channel;
import com.abs.notification.domain.vo.NotificationStatus;
import com.abs.notification.infrastructure.email.EmailSender;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepo;
    private final NotificationTemplateRepository templateRepo;
    private final TemplateRenderer renderer;
    private final EmailSender emailSender;
    private final MeterRegistry meterRegistry;

    @Transactional
    public NotificationAggregate sendEmail(SendEmailCommand cmd) {
        String locale = cmd.locale() == null ? "vi" : cmd.locale();
        NotificationTemplateAggregate template = templateRepo
                .findByCodeAndLocaleAndChannel(cmd.templateCode(), locale, Channel.EMAIL)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Template not found: " + cmd.templateCode() + "/" + locale + "/EMAIL"));

        Map<String, Object> vars = cmd.variables() == null ? Map.of() : cmd.variables();
        String subject = renderer.render(template.getSubject(), vars);
        String body    = renderer.render(template.getBody(), vars);

        NotificationAggregate record = NotificationAggregate.builder()
                .templateCode(cmd.templateCode())
                .userId(cmd.userId())
                .channel(Channel.EMAIL)
                .recipient(cmd.recipient())
                .variables(vars)
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .build();
        record = notificationRepo.save(record);

        try {
            emailSender.send(cmd.recipient(), subject, body);
            record.markSent(LocalDateTime.now());
            record = notificationRepo.save(record);
            counter("sent", "email").increment();
            log.info("Email sent: tmpl={} to={}", cmd.templateCode(), cmd.recipient());
        } catch (Exception ex) {
            record.markFailed(ex.getMessage());
            notificationRepo.save(record);
            counter("failed", "email").increment();
            log.error("Email send failed: tmpl={} to={} err={}",
                    cmd.templateCode(), cmd.recipient(), ex.getMessage());
            throw ex;
        }
        return record;
    }

    @Transactional
    public NotificationAggregate sendSms(SendSmsCommand cmd) {
        String locale = cmd.locale() == null ? "vi" : cmd.locale();
        NotificationTemplateAggregate template = templateRepo
                .findByCodeAndLocaleAndChannel(cmd.templateCode(), locale, Channel.SMS)
                .orElseThrow(() -> new IllegalArgumentException(
                        "SMS template not found: " + cmd.templateCode()));

        Map<String, Object> vars = cmd.variables() == null ? Map.of() : cmd.variables();
        String body = renderer.render(template.getBody(), vars);

        NotificationAggregate record = NotificationAggregate.builder()
                .templateCode(cmd.templateCode())
                .userId(cmd.userId())
                .channel(Channel.SMS)
                .recipient(cmd.recipient())
                .variables(vars)
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .build();
        record = notificationRepo.save(record);

        // Dev: SMS is stubbed — log only. Replace with Twilio adapter in prod.
        log.info("[SMS-STUB] to={} body={}", cmd.recipient(), body);
        record.markSent(LocalDateTime.now());
        record = notificationRepo.save(record);
        counter("sent", "sms").increment();
        return record;
    }

    private Counter counter(String outcome, String channel) {
        return Counter.builder("notification.dispatched")
                .tag("outcome", outcome)
                .tag("channel", channel)
                .register(meterRegistry);
    }
}
