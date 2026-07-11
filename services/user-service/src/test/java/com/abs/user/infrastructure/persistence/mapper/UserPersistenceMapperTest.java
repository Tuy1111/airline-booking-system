package com.abs.user.infrastructure.persistence.mapper;

import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.vo.EmailAddress;
import com.abs.user.domain.vo.PasswordHash;
import com.abs.user.domain.vo.PersonName;
import com.abs.user.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserPersistenceMapperTest {

    @Test
    void preservesPassengerSharedPrimaryKeyWhenMappingExistingUser() {
        UserAggregate registered = UserAggregate.register(
                EmailAddress.of("passenger-id@example.com"),
                PasswordHash.of("$2a$10$abcdefghijklmnopqrstuuABCDEFGHIJKLMNOPQRSTUVWXYZ012"),
                PersonName.of("Test User"),
                null,
                LocalDateTime.now());

        UserEntity inserted = UserPersistenceMapper.toEntity(registered);
        inserted.setId(42L);
        inserted.getPassenger().setUserId(42L);
        UserAggregate existing = UserPersistenceMapper.toAggregate(inserted);

        UserEntity update = UserPersistenceMapper.toEntity(existing);

        assertThat(update.getPassenger().getUserId()).isEqualTo(42L);
        assertThat(update.getPassenger().getUser()).isSameAs(update);
    }
}
