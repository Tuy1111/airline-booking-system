package com.abs.user.infrastructure.adapter;

import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.repository.UserRepository;
import com.abs.user.domain.vo.EmailAddress;
import com.abs.user.domain.vo.UserId;
import com.abs.user.infrastructure.persistence.UserJpaRepository;
import com.abs.user.infrastructure.persistence.entity.UserEntity;
import com.abs.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Driven adapter implementing the {@link UserRepository} port on top of Spring Data JPA. It unwraps
 * the domain Value Objects at the boundary and delegates all aggregate &lt;-&gt; row translation to
 * {@link UserPersistenceMapper}.
 */
@Component
@RequiredArgsConstructor
@Transactional
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository repository;
    private final EntityManager entityManager;

    @Override
    public UserAggregate save(UserAggregate user) {
        UserEntity entity = UserPersistenceMapper.toEntity(user);
        if (entity.getId() != null && !repository.existsById(entity.getId())) {
            entityManager.persist(entity);
            return UserPersistenceMapper.toAggregate(entity);
        }
        return UserPersistenceMapper.toAggregate(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAggregate> findById(UserId id) {
        return repository.findById(id.value()).map(UserPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAggregate> findByEmail(EmailAddress email) {
        return repository.findByEmail(email.value()).map(UserPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(EmailAddress email) {
        return repository.existsByEmail(email.value());
    }
}
