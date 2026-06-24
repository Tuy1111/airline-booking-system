package com.abs.user.application.usecase;

import com.abs.user.application.command.AuthenticateUserCommand;
import com.abs.user.application.dto.AuthResult;
import com.abs.user.application.dto.AuthToken;
import com.abs.user.application.dto.UserView;
import com.abs.user.application.exception.AuthenticationFailedException;
import com.abs.user.application.port.PasswordHasher;
import com.abs.user.application.port.TokenIssuer;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.EmailAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Use case: authenticate a user and record the resulting authentication state.
 *
 * <p>On a wrong password the aggregate records a failed attempt (which auto-locks after the
 * threshold); on success it stamps the login time and clears the counter. The thrown exception is
 * deliberately uniform so callers cannot distinguish "unknown email" from "wrong password".
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    @Transactional
    public AuthResult handle(AuthenticateUserCommand command) {
        EmailAddress email = EmailAddress.of(command.email());
        log.info("Handling Authenticate command: email={}", email.value());

        Optional<UserAggregate> found = userRepository.findByEmail(email);
        if (found.isEmpty()) {
            log.warn("Authentication failed — unknown email: email={}", email.value());
            throw new AuthenticationFailedException();
        }

        UserAggregate user = found.get();
        if (!user.getStatus().canAuthenticate()) {
            log.warn("Authentication blocked — account not active: userId={}, status={}",
                    user.getId(), user.getStatus());
            throw new AuthenticationFailedException();
        }

        if (!passwordHasher.matches(command.rawPassword(), user.getPasswordHash())) {
            user.recordFailedLoginAttempt();
            userRepository.save(user);
            throw new AuthenticationFailedException();
        }

        user.recordSuccessfulLogin(LocalDateTime.now());
        UserAggregate saved = userRepository.save(user);

        AuthToken token = tokenIssuer.issue(saved);
        log.info("User authenticated: userId={}", saved.getId());
        return AuthResult.of(token, UserView.from(saved));
    }
}
