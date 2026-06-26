package com.abs.user.domain.repository;

import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.vo.EmailAddress;
import com.abs.user.domain.vo.UserId;

import java.util.Optional;

/**
 * Outbound port for persisting and retrieving the {@link UserAggregate}.
 *
 * <p>There is exactly one repository, for the single aggregate root. The passenger profile and the
 * loyalty membership are internal entities of that aggregate and are deliberately <em>not</em>
 * exposed through their own repositories — they are always loaded and saved as part of the whole
 * {@code User}, which keeps the transactional consistency boundary at the root.
 */
public interface UserRepository {

    UserAggregate save(UserAggregate user);

    Optional<UserAggregate> findById(UserId id);

    Optional<UserAggregate> findByEmail(EmailAddress email);

    boolean existsByEmail(EmailAddress email);
}
