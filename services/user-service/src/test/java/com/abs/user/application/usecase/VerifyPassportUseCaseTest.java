package com.abs.user.application.usecase;

import com.abs.user.application.command.SubmitPassportCommand;
import com.abs.user.application.dto.UserView;
import com.abs.user.application.port.out.KycVerificationPort;
import com.abs.user.application.port.out.NotificationPort;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.EmailAddress;
import com.abs.user.domain.vo.PasswordHash;
import com.abs.user.domain.vo.PersonName;
import com.abs.user.domain.vo.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerifyPassportUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private KycVerificationPort kycVerificationPort;
    @Mock
    private NotificationPort notificationPort;

    private VerifyPassportUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new VerifyPassportUseCase(userRepository, kycVerificationPort, notificationPort);
    }

    @Test
    void automaticallyVerifiesPassportAndSendsNotification() {
        UserAggregate user = registeredUser();
        when(userRepository.findById(UserId.of(42L))).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserAggregate.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(kycVerificationPort.verify(any())).thenReturn(new KycVerificationPort.VerificationResult(
                KycVerificationPort.Decision.VERIFIED, "matched"));

        UserView result = useCase.submitPassport(new SubmitPassportCommand(
                42L, "B1234567", "VNM", LocalDate.now().plusYears(2)));

        assertThat(result.kycStatus()).isEqualTo("VERIFIED");
        verify(userRepository, times(2)).save(user);
        ArgumentCaptor<NotificationPort.NotificationMessage> notification =
                ArgumentCaptor.forClass(NotificationPort.NotificationMessage.class);
        verify(notificationPort).send(notification.capture());
        assertThat(notification.getValue().userId()).isEqualTo(42L);
        assertThat(notification.getValue().type()).isEqualTo("KYC_VERIFIED");
    }

    @Test
    void automaticallyRejectsFlaggedPassportAndSendsNotification() {
        UserAggregate user = registeredUser();
        when(userRepository.findById(UserId.of(42L))).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserAggregate.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(kycVerificationPort.verify(any())).thenReturn(new KycVerificationPort.VerificationResult(
                KycVerificationPort.Decision.REJECTED, "flagged"));

        UserView result = useCase.submitPassport(new SubmitPassportCommand(
                42L, "REJECT123", "VNM", LocalDate.now().plusYears(2)));

        assertThat(result.kycStatus()).isEqualTo("REJECTED");
        verify(userRepository, times(2)).save(user);
        ArgumentCaptor<NotificationPort.NotificationMessage> notification =
                ArgumentCaptor.forClass(NotificationPort.NotificationMessage.class);
        verify(notificationPort).send(notification.capture());
        assertThat(notification.getValue().type()).isEqualTo("KYC_REJECTED");
        assertThat(notification.getValue().content()).contains("flagged");
    }

    private UserAggregate registeredUser() {
        return UserAggregate.register(
                EmailAddress.of("kyc@example.com"),
                PasswordHash.of("$2a$10$abcdefghijklmnopqrstuuABCDEFGHIJKLMNOPQRSTUVWXYZ012"),
                PersonName.of("KYC User"),
                null,
                LocalDateTime.now());
    }
}
