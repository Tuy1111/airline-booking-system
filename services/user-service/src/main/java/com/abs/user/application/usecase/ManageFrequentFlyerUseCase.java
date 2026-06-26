package com.abs.user.application.usecase;

import com.abs.user.application.command.EarnMilesCommand;
import com.abs.user.application.command.RedeemMilesCommand;
import com.abs.user.application.dto.UserView;
import com.abs.user.application.exception.UserNotFoundException;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.FrequentFlyerMiles;
import com.abs.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Discovered use case: manage frequent-flyer tier by earning and redeeming miles. All tier
 * recalculation and balance invariants live in the aggregate; this handler only orchestrates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ManageFrequentFlyerUseCase {

    private final UserRepository userRepository;

    @Transactional
    public UserView earnMiles(EarnMilesCommand command) {
        log.info("Handling EarnMiles command: userId={}, miles={}, reason={}",
                command.userId(), command.miles(), command.reason());

        UserAggregate user = load(command.userId());
        user.earnMiles(FrequentFlyerMiles.of(command.miles()));

        UserAggregate saved = userRepository.save(user);
        log.info("Miles earned: userId={}, tier={}, balance={}",
                saved.getId(), saved.getLoyalty().getTier(), saved.getLoyalty().getMilesBalance().value());
        return UserView.from(saved);
    }

    @Transactional
    public UserView redeemMiles(RedeemMilesCommand command) {
        log.info("Handling RedeemMiles command: userId={}, miles={}, reason={}",
                command.userId(), command.miles(), command.reason());

        UserAggregate user = load(command.userId());
        user.redeemMiles(FrequentFlyerMiles.of(command.miles()));

        UserAggregate saved = userRepository.save(user);
        log.info("Miles redeemed: userId={}, balance={}",
                saved.getId(), saved.getLoyalty().getMilesBalance().value());
        return UserView.from(saved);
    }

    private UserAggregate load(Long userId) {
        return userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
