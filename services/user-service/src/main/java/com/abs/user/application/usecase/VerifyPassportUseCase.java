package com.abs.user.application.usecase;

import com.abs.user.application.command.SubmitPassportCommand;
import com.abs.user.application.dto.UserView;
import com.abs.user.application.exception.UserNotFoundException;
import com.abs.user.application.port.out.KycVerificationPort;
import com.abs.user.application.port.out.KycVerificationPort.VerificationResult;
import com.abs.user.application.port.out.NotificationPort;
import com.abs.user.application.port.out.NotificationPort.NotificationMessage;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.CountryCode;
import com.abs.user.domain.vo.Passport;
import com.abs.user.domain.vo.PassportNumber;
import com.abs.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Discovered use case: KYC / passport verification. Splits the workflow into the passenger-driven
 * step (submit a passport for review) and the back-office steps (approve / reject). The expiry and
 * "passport present" invariants are enforced inside the aggregate's profile entity.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VerifyPassportUseCase {

    private final UserRepository userRepository;
    private final KycVerificationPort kycVerificationPort;
    private final NotificationPort notificationPort;

    public UserView submitPassport(SubmitPassportCommand command) {
        log.info("Handling SubmitPassport command: userId={}, issuingCountry={}",
                command.userId(), command.issuingCountry());

        UserAggregate user = load(command.userId());
        Passport passport = Passport.of(
                PassportNumber.of(command.passportNumber()),
                CountryCode.of(command.issuingCountry()),
                command.expiryDate());
        user.submitPassport(passport, LocalDate.now());

        UserAggregate pending = userRepository.save(user);
        log.info("Passport submitted: userId={}, kycStatus={}",
                pending.getId(), pending.getPassengerProfile().getKycStatus());

        VerificationResult result = kycVerificationPort.verify(passport);
        if (result.isVerified()) {
            pending.verifyPassport(LocalDate.now());
        } else {
            pending.rejectPassport();
        }

        UserAggregate saved = userRepository.save(pending);
        log.info("Automatic KYC completed: userId={}, status={}, reason={}",
                saved.getId(), saved.getPassengerProfile().getKycStatus(), result.reason());
        notifyKycResult(command.userId(), result);
        return UserView.from(saved);
    }

    @Transactional
    public UserView approve(Long userId) {
        log.info("Handling KYC approval: userId={}", userId);
        UserAggregate user = load(userId);
        user.verifyPassport(LocalDate.now());
        UserAggregate saved = userRepository.save(user);
        log.info("KYC approved: userId={}", saved.getId());
        return UserView.from(saved);
    }

    @Transactional
    public UserView reject(Long userId) {
        log.info("Handling KYC rejection: userId={}", userId);
        UserAggregate user = load(userId);
        user.rejectPassport();
        UserAggregate saved = userRepository.save(user);
        log.info("KYC rejected: userId={}", saved.getId());
        return UserView.from(saved);
    }

    private UserAggregate load(Long userId) {
        return userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void notifyKycResult(Long userId, VerificationResult result) {
        String type = result.isVerified() ? "KYC_VERIFIED" : "KYC_REJECTED";
        String title = result.isVerified() ? "Passport verification successful" : "Passport verification failed";
        String content = result.isVerified()
                ? "Your passport has been verified successfully."
                : "Your passport could not be verified: " + result.reason();
        try {
            notificationPort.send(new NotificationMessage(userId, title, content, type));
        } catch (RuntimeException ex) {
            // Notification delivery is best-effort and must not undo an already persisted KYC decision.
            log.warn("Could not notify user about KYC result: userId={}, type={}", userId, type, ex);
        }
    }
}
