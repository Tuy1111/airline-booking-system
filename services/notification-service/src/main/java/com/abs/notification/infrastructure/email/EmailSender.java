package com.abs.notification.infrastructure.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;

    @Value("${app.notification.from-address}")
    private String fromAddress;

    /**
     * Gửi email plain-text. Dùng {@link MimeMessage} + UTF-8 vì subject/body có
     * dấu tiếng Việt ("Xác nhận…") — {@code SimpleMailMessage} dễ bị mojibake với
     * một số SMTP server.
     */
    public void send(String to, String subject, String body) {
        MimeMessage mime = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(mime, false, StandardCharsets.UTF_8.name());
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
        } catch (MessagingException e) {
            throw new IllegalStateException("Cannot build email to " + to, e);
        }
        mailSender.send(mime);
    }
}
