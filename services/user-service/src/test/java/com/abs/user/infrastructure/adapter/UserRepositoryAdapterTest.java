package com.abs.user.infrastructure.adapter;

import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.vo.EmailAddress;
import com.abs.user.domain.vo.PasswordHash;
import com.abs.user.domain.vo.PersonName;
import com.abs.user.domain.vo.UserId;
import com.abs.user.infrastructure.persistence.UserJpaRepository;
import com.abs.user.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository repository;

    @Mock
    private EntityManager entityManager;

    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new UserRepositoryAdapter(repository, entityManager);
    }

    @Test
    void persistsAssignedIdWhenUserDoesNotExist() {
        UserAggregate user = registeredUser();
        when(repository.existsById(42L)).thenReturn(false);

        UserAggregate saved = adapter.save(user);

        verify(entityManager).persist(any(UserEntity.class));
        verify(repository, never()).save(any(UserEntity.class));
        assertThat(saved.getId()).isEqualTo(UserId.of(42L));
    }

    @Test
    void mergesAssignedIdWhenUserAlreadyExists() {
        UserAggregate user = registeredUser();
        when(repository.existsById(42L)).thenReturn(true);
        when(repository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        adapter.save(user);

        verify(repository).save(any(UserEntity.class));
        verifyNoInteractions(entityManager);
    }

    private UserAggregate registeredUser() {
        return UserAggregate.register(
                UserId.of(42L),
                EmailAddress.of("assigned-id@example.com"),
                PasswordHash.of("$2a$10$abcdefghijklmnopqrstuuABCDEFGHIJKLMNOPQRSTUVWXYZ012"),
                PersonName.of("Assigned Id"),
                null,
                LocalDateTime.now());
    }
}
