package com.abs.notification.application.usecase;

import com.abs.notification.domain.aggregate.Notification;
import com.abs.notification.domain.enums.Channel;
import com.abs.notification.domain.enums.NotificationStatus;
import com.abs.notification.domain.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendDirectNotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    void recordsDirectNotificationAsSentPushMessage() {
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        SendDirectNotificationService service = new SendDirectNotificationService(notificationRepository);

        Notification result = service.send(42L, "KYC complete", "Passport verified", "kyc_verified");

        assertThat(result.getTemplateCode()).isEqualTo("KYC_VERIFIED");
        assertThat(result.getUserId()).isEqualTo(42L);
        assertThat(result.getRecipient().channel()).isEqualTo(Channel.PUSH);
        assertThat(result.getRecipient().address()).isEqualTo("user:42");
        assertThat(result.getStatus()).isEqualTo(NotificationStatus.SENT);
        assertThat(result.getSentAt()).isNotNull();
        assertThat(result.getVariables())
                .containsEntry("title", "KYC complete")
                .containsEntry("content", "Passport verified");
    }
}
