package com.abs.user.api;

import com.abs.user.application.exception.UserNotFoundException;
import com.abs.user.domain.exception.AccountDeletedException;
import com.abs.user.domain.exception.AccountLockedException;
import com.abs.user.domain.exception.DomainException;
import com.abs.user.domain.exception.InsufficientMilesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Translates domain and application exceptions into HTTP responses. The domain stays oblivious to
 * HTTP; this driving-side adapter is the single place that decides which violated invariant maps to
 * which status code. More specific handlers win over the {@link DomainException} catch-all.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public record ErrorResponse(int status, String error, String message) {
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> handleLocked(AccountLockedException ex) {
        return build(HttpStatus.LOCKED, ex.getMessage());
    }

    @ExceptionHandler(AccountDeletedException.class)
    public ResponseEntity<ErrorResponse> handleDeleted(AccountDeletedException ex) {
        return build(HttpStatus.GONE, ex.getMessage());
    }

    @ExceptionHandler(InsufficientMilesException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientMiles(InsufficientMilesException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Catch-all for the remaining domain validation/invariant violations. */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        log.warn("Request failed: status={}, message={}", status.value(), message);
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), status.getReasonPhrase(), message));
    }
}
