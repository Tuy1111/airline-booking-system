package com.abs.user.infrastructure.persistence.mapper;

import com.abs.user.domain.aggregate.PassengerAggregate;
import com.abs.user.infrastructure.persistence.entity.PassengerEntity;
import com.abs.user.infrastructure.persistence.entity.UserEntity;

public final class PassengerPersistenceMapper {
    private PassengerPersistenceMapper() {
    }

    public static PassengerAggregate toAggregate(PassengerEntity entity) {
        return PassengerAggregate.builder()
                .userId(entity.getUserId())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .dateOfBirth(entity.getDateOfBirth())
                .gender(entity.getGender())
                .passportNo(entity.getPassportNo())
                .nationality(entity.getNationality())
                .build();
    }

    public static PassengerEntity toEntity(PassengerAggregate aggregate, UserEntity user) {
        return PassengerEntity.builder()
                .userId(aggregate.getUserId())
                .user(user)
                .fullName(aggregate.getFullName())
                .phone(aggregate.getPhone())
                .dateOfBirth(aggregate.getDateOfBirth())
                .gender(aggregate.getGender())
                .passportNo(aggregate.getPassportNo())
                .nationality(aggregate.getNationality())
                .build();
    }
}
