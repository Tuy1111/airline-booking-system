package com.abs.user.application.usecase;

import com.abs.user.application.command.DeleteAccountCommand;
import com.abs.user.application.exception.UserNotFoundException;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Discovered use case: GDPR account deletion (right to be forgotten).
 *
 * <p>The account is anonymised and moved to the terminal {@code DELETED} state rather than being
 * physically removed, so logical references held by other services (booking, payment) stay intact
 * while all PII is erased. Idempotency is handled by the aggregate: re-deleting a deleted account
 * raises {@code AccountDeletedException}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteUserAccountUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void handle(DeleteAccountCommand command) {
        log.info("Handling DeleteAccount command: userId={}, reason={}",
                command.userId(), command.reason());

        UserAggregate user = userRepository.findById(UserId.of(command.userId()))
                .orElseThrow(() -> new UserNotFoundException(command.userId()));

        user.requestErasure(LocalDateTime.now());
        userRepository.save(user);

        log.info("Account erased (GDPR): userId={}", command.userId());
    }
}
