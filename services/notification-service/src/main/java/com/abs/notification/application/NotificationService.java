package com.abs.notification.application;

import com.abs.notification.application.dto.SendEmailCommand;
import com.abs.notification.application.dto.SendSmsCommand;
import com.abs.notification.application.port.in.SendEmailUseCase;
import com.abs.notification.application.port.in.SendSmsUseCase;
import com.abs.notification.domain.aggregate.Notification;
import com.abs.notification.domain.aggregate.NotificationTemplate;
import com.abs.notification.domain.repository.NotificationRepository;
import com.abs.notification.domain.repository.NotificationTemplateRepository;
import com.abs.notification.domain.enums.Channel;
import com.abs.notification.domain.enums.NotificationStatus;
import com.abs.notification.domain.vo.Recipient;
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
public class NotificationService implements SendEmailUseCase, SendSmsUseCase {

    private final NotificationRepository notificationRepo;
    private final NotificationTemplateRepository templateRepo;
    private final TemplateRenderer renderer;
    private final EmailSender emailSender;
    private final MeterRegistry meterRegistry;

    /**
     * Gửi email theo template.
     *
     * <p>Lưu ý thiết kế:
     * <ul>
     *   <li><b>Không</b> bọc cả method trong 1 transaction: việc gọi SMTP (I/O mạng)
     *       không được giữ DB connection, và bản ghi audit FAILED phải tồn tại kể cả
     *       khi gửi lỗi (mỗi {@code save} là một transaction riêng).</li>
     *   <li><b>Idempotency:</b> nếu {@code dedupKey} đã xử lý trước đó thì trả về bản
     *       ghi cũ, không gửi lại — tránh email trùng khi Kafka/Rabbit redelivery.</li>
     *   <li>Lỗi gửi được nuốt (log + đánh FAILED) thay vì ném ra, để consumer bất
     *       đồng bộ không retry vô hạn rồi gửi trùng.</li>
     * </ul>
     */
    @Override
    public Notification sendEmail(SendEmailCommand cmd) {
        if (cmd.dedupKey() != null) {
            var existing = notificationRepo.findByDedupKey(cmd.dedupKey());
            if (existing.isPresent()) {
                log.info("Duplicate event skipped: dedupKey={}", cmd.dedupKey());
                counter("deduped", "email").increment();
                return existing.get();
            }
        }

        String locale = cmd.locale() == null ? "vi" : cmd.locale();
        NotificationTemplate template = templateRepo
                .findByCodeAndLocaleAndChannel(cmd.templateCode(), locale, Channel.EMAIL)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Template not found: " + cmd.templateCode() + "/" + locale + "/EMAIL"));

        Map<String, Object> vars = cmd.variables() == null ? Map.of() : cmd.variables();
        String subject = renderer.render(template.getSubject(), vars);
        String body    = renderer.render(template.getBody(), vars);

        Notification record = Notification.builder()
                .templateCode(cmd.templateCode())
                .userId(cmd.userId())
                .recipient(Recipient.of(Channel.EMAIL, cmd.recipient()))
                .variables(vars)
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .dedupKey(cmd.dedupKey())
                .build();
        record = notificationRepo.save(record);

        try {
            emailSender.send(cmd.recipient(), subject, body);
            record.markSent(LocalDateTime.now());
            counter("sent", "email").increment();
            log.info("Email sent: tmpl={} to={}", cmd.templateCode(), cmd.recipient());
        } catch (Exception ex) {
            record.markFailed(ex.getMessage());
            counter("failed", "email").increment();
            log.error("Email send failed: tmpl={} to={} err={}",
                    cmd.templateCode(), cmd.recipient(), ex.getMessage());
        }
        return notificationRepo.save(record);
    }

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
