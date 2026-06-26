package com.abs.user.application.usecase;

import com.abs.user.application.command.RegisterUserCommand;
import com.abs.user.application.dto.AuthResult;
import com.abs.user.application.dto.AuthToken;
import com.abs.user.application.dto.UserView;
import com.abs.user.application.exception.EmailAlreadyRegisteredException;
import com.abs.user.application.port.PasswordHasher;
import com.abs.user.application.port.TokenIssuer;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.EmailAddress;
import com.abs.user.domain.vo.PasswordHash;
import com.abs.user.domain.vo.PersonName;
import com.abs.user.domain.vo.PhoneNumber;
import com.abs.user.domain.vo.RawPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Use case: register a new account.
 *
 * <p>Enforces the cross-aggregate uniqueness rule (email not already taken), delegates strength
 * validation and hashing to the domain VO / port, and lets the {@link UserAggregate#register}
 * factory establish the creation invariants.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    @Transactional
    public AuthResult handle(RegisterUserCommand command) {
        log.info("Handling RegisterUser command: email={}", command.email());

        EmailAddress email = EmailAddress.of(command.email());
        if (userRepository.existsByEmail(email)) {
            log.warn("Registration rejected — email already in use: email={}", email.value());
            throw new EmailAlreadyRegisteredException(email.value());
        }

        PasswordHash passwordHash = passwordHasher.hash(RawPassword.of(command.rawPassword()));
        PersonName fullName = PersonName.of(command.fullName());
        PhoneNumber phone = isBlank(command.phone()) ? null : PhoneNumber.of(command.phone());

        UserAggregate user = UserAggregate.register(email, passwordHash, fullName, phone, LocalDateTime.now());
        UserAggregate saved = userRepository.save(user);

        AuthToken token = tokenIssuer.issue(saved);
        log.info("User registered: userId={}, email={}", saved.getId(), email.value());
        return AuthResult.of(token, UserView.from(saved));
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
