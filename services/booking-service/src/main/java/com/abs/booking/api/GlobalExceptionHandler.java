package com.abs.booking.api;

import com.abs.booking.domain.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(
            String code,
            String message,
            LocalDateTime timestamp
    ) {}

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(BookingNotFoundException ex) {
        log.warn("Booking not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("BOOKING_NOT_FOUND", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SeatAlreadyHeldException.class)
    public ResponseEntity<ErrorResponse> handleSeatAlreadyHeld(SeatAlreadyHeldException ex) {
        log.warn("Seat already held: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("SEAT_ALREADY_HELD", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(SeatNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleSeatNotAvailable(SeatNotAvailableException ex) {
        log.warn("Seat not available: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("SEAT_NOT_AVAILABLE", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidBookingStateException ex) {
        log.warn("Invalid booking state: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("INVALID_BOOKING_STATE", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UnauthorizedBookingAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(UnauthorizedBookingAccessException ex) {
        log.warn("Unauthorized booking access: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("UNAUTHORIZED_ACCESS", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(BookingDomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(BookingDomainException ex) {
        log.error("Domain exception occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse("DOMAIN_ERROR", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
