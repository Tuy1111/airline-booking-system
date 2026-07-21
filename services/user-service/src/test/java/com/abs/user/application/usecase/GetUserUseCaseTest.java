package com.abs.user.application.usecase;

import com.abs.user.application.dto.UserView;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.EmailAddress;
import com.abs.user.domain.vo.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void provisionsCurrentKeycloakUserWithTrustedIdentity() {
        when(userRepository.findById(UserId.of(42L))).thenReturn(Optional.empty());
        when(userRepository.existsByEmail(EmailAddress.of("linh@example.com"))).thenReturn(false);
        when(userRepository.save(any(UserAggregate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserView result = new GetUserUseCase(userRepository)
                .findOrCreateCurrent(42L, "linh@example.com", "Nguyễn Linh");

        assertThat(result.id()).isEqualTo(42L);
        assertThat(result.email()).isEqualTo("linh@example.com");
        assertThat(result.fullName()).isEqualTo("Nguyễn Linh");
        verify(userRepository).save(any(UserAggregate.class));
    }
}
