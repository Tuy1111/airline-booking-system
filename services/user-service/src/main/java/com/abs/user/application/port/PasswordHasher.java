package com.abs.user.application.port;

import com.abs.user.domain.vo.PasswordHash;
import com.abs.user.domain.vo.RawPassword;

/**
 * Outbound port for credential hashing.
 *
 * <p>The actual algorithm (PBKDF2, bcrypt, ...) is an infrastructure detail that lives behind this
 * interface, so neither the domain nor the use cases depend on a concrete crypto library. Hashing
 * accepts a strength-validated {@link RawPassword}; verification accepts the raw string as typed at
 * login (no strength policy is re-applied when merely checking a match).
 */
public interface PasswordHasher {

    PasswordHash hash(RawPassword rawPassword);

    boolean matches(String rawPassword, PasswordHash passwordHash);
}
