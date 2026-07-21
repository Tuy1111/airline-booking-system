package com.abs.notification.application.usecase;

import com.abs.notification.application.TemplateRenderer;
import com.abs.notification.application.dto.SendSmsCommand;
import com.abs.notification.application.port.in.SendSmsUseCase;
import com.abs.notification.domain.aggregate.Notification;
import com.abs.notification.domain.aggregate.NotificationTemplate;
import com.abs.notification.domain.enums.Channel;
import com.abs.notification.domain.enums.NotificationStatus;
import com.abs.notification.domain.repository.NotificationRepository;
import com.abs.notification.domain.repository.NotificationTemplateRepository;
import com.abs.notification.domain.vo.Recipient;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Hiện thực use case "gửi SMS theo template" ({@link SendSmsUseCase}).
 *
 * <p>Bản dev đang stub SMS (chỉ log). Thay bằng adapter Twilio ở môi trường prod.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SendSmsService implements SendSmsUseCase {

    private final NotificationRepository notificationRepo;
    private final NotificationTemplateRepository templateRepo;
    private final TemplateRenderer renderer;
    private final MeterRegistry meterRegistry;

    @Override
    @Transactional
    public Notification sendSms(SendSmsCommand cmd) {
        String locale = cmd.locale() == null ? "vi" : cmd.locale();
        NotificationTemplate template = templateRepo
                .findByCodeAndLocaleAndChannel(cmd.templateCode(), locale, Channel.SMS)
                .orElseThrow(() -> new IllegalArgumentException(
                        "SMS template not found: " + cmd.templateCode()));

        Map<String, Object> vars = cmd.variables() == null ? Map.of() : cmd.variables();
        String body = renderer.render(template.getBody(), vars);

        Notification record = Notification.builder()
                .templateCode(cmd.templateCode())
                .userId(cmd.userId())
                .recipient(Recipient.of(Channel.SMS, cmd.recipient()))
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
