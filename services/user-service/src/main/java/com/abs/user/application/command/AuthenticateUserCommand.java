package com.abs.user.application.command;

/** Input for verifying credentials and recording authentication state (UC: Authenticate User). */
public record AuthenticateUserCommand(String email, String rawPassword) {
}
