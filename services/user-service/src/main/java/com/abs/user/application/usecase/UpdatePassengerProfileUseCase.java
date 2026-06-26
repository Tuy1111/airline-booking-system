package com.abs.user.application.usecase;

import com.abs.user.application.command.UpdatePassengerProfileCommand;
import com.abs.user.application.dto.UserView;
import com.abs.user.application.exception.UserNotFoundException;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.CountryCode;
import com.abs.user.domain.vo.DateOfBirth;
import com.abs.user.domain.vo.PersonName;
import com.abs.user.domain.vo.PhoneNumber;
import com.abs.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case: create/update the passenger profile attached to an account. Builds the VOs from the
 * command (each validating itself) and applies the change through the aggregate root so the profile
 * entity is never mutated directly.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePassengerProfileUseCase {

    private final UserRepository userRepository;

    @Transactional
    public UserView handle(UpdatePassengerProfileCommand command) {
        log.info("Handling UpdatePassengerProfile command: userId={}", command.userId());

        UserAggregate user = userRepository.findById(UserId.of(command.userId()))
                .orElseThrow(() -> new UserNotFoundException(command.userId()));

        user.updatePersonalDetails(
                PersonName.of(command.fullName()),
                command.dateOfBirth() == null ? null : DateOfBirth.of(command.dateOfBirth()),
                command.gender(),
                isBlank(command.nationality()) ? null : CountryCode.of(command.nationality()));

        user.updateContactDetails(isBlank(command.phone()) ? null : PhoneNumber.of(command.phone()));

        UserAggregate saved = userRepository.save(user);
        log.info("Passenger profile updated: userId={}", saved.getId());
        return UserView.from(saved);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
