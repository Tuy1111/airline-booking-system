package com.abs.user.application.usecase;

import com.abs.user.application.dto.UserView;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.EmailAddress;
import com.abs.user.domain.vo.PasswordHash;
import com.abs.user.domain.vo.PersonName;
import com.abs.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.time.LocalDateTime;

/** Query use case: fetch a user projection by id. */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetUserUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<UserView> findById(Long id) {
        log.debug("Handling GetUser query: userId={}", id);
        return userRepository.findById(UserId.of(id)).map(UserView::from);
    }

    @Transactional
    public UserView findOrCreateCurrent(Long id, String email, String fullName) {
        UserId userId = UserId.of(id);
        EmailAddress emailAddress = EmailAddress.of(email);

        Optional<UserAggregate> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            UserAggregate user = byId.get();
            if (!user.getEmail().equals(emailAddress)) {
                throw new IllegalStateException("Authenticated user id belongs to another email");
            }
            return UserView.from(user);
        }

        if (userRepository.existsByEmail(emailAddress)) {
            throw new IllegalStateException("Authenticated email belongs to another user id");
        }

        String displayName = fullName == null || fullName.isBlank()
                ? email.substring(0, email.indexOf('@')).replaceAll("[._-]+", " ")
                : fullName;
        UserAggregate user = UserAggregate.register(
                userId,
                emailAddress,
                PasswordHash.of("KEYCLOAK_MANAGED"),
                PersonName.of(displayName),
                null,
                LocalDateTime.now());
        return UserView.from(userRepository.save(user));
    }
}
