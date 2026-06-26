package com.abs.user.infrastructure.security;

import com.abs.user.application.port.PasswordHasher;
import com.abs.user.domain.vo.PasswordHash;
import com.abs.user.domain.vo.RawPassword;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * {@link PasswordHasher} implementation backed by BCrypt (Spring Security's standalone crypto
 * module). BCrypt embeds the salt and cost factor in its 60-character output, which fits the 255-char
 * credential column. Verification of a non-BCrypt stored value (e.g. the {@code DELETED} placeholder
 * on an erased account) simply returns {@code false}.
 */
@Component
public class BcryptPasswordHasher implements PasswordHasher {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public PasswordHash hash(RawPassword rawPassword) {
        return PasswordHash.of(encoder.encode(rawPassword.value()));
    }

    @Override
    public boolean matches(String rawPassword, PasswordHash passwordHash) {
        if (rawPassword == null || passwordHash == null) {
            return false;
        }
        try {
            return encoder.matches(rawPassword, passwordHash.value());
        } catch (IllegalArgumentException ex) {
            // Stored value is not a valid BCrypt hash (e.g. anonymised/erased account).
            return false;
        }
    }
}
