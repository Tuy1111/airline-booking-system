package com.abs.user.application.port;

import com.abs.user.application.dto.AuthToken;
import com.abs.user.domain.aggregate.UserAggregate;

/**
 * Outbound port for issuing an authentication token for a user. The signing scheme (JWT/HS256, key
 * management, TTL) is an infrastructure detail hidden behind this interface.
 */
public interface TokenIssuer {

    AuthToken issue(UserAggregate user);
}
