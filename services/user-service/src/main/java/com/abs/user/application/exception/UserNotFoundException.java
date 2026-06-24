package com.abs.user.application.exception;

/** Raised when a use case is asked to operate on a user id that does not exist. */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super("User not found: " + userId);
    }
}
