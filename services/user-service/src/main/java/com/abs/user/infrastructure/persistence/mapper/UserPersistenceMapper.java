package com.abs.user.infrastructure.persistence.mapper;

import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.infrastructure.persistence.entity.PassengerEntity;
import com.abs.user.infrastructure.persistence.entity.UserEntity;

public final class UserPersistenceMapper {
    private UserPersistenceMapper() {
    }

    public static UserAggregate toAggregate(UserEntity entity) {
        return UserAggregate.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .lastLoginAt(entity.getLastLoginAt())
                .passenger(entity.getPassenger() == null
                        ? null
                        : PassengerPersistenceMapper.toAggregate(entity.getPassenger()))
                .roles(entity.getRoles())
                .build();
    }

    public static UserEntity toEntity(UserAggregate aggregate) {
        UserEntity entity = UserEntity.builder()
                .id(aggregate.getId())
                .email(aggregate.getEmail())
                .passwordHash(aggregate.getPasswordHash())
                .status(aggregate.getStatus())
                .createdAt(aggregate.getCreatedAt())
                .lastLoginAt(aggregate.getLastLoginAt())
                .roles(aggregate.getRoles())
                .build();
        if (aggregate.getPassenger() != null) {
            PassengerEntity passenger =
                    PassengerPersistenceMapper.toEntity(aggregate.getPassenger(), entity);
            entity.setPassenger(passenger);
        }
        return entity;
    }
}
