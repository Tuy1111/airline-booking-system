package com.abs.user.application.command;

/** Input for registering a new account (UC: Register User Account). */
public record RegisterUserCommand(String email, String rawPassword, String fullName, String phone) {
}
