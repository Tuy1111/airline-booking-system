package com.abs.user.application.usecase;

import com.abs.user.application.dto.UserView;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
}
