package com.abs.booking.domain.exception;

public class UnauthorizedBookingAccessException extends BookingDomainException {
    public UnauthorizedBookingAccessException(Long userId, Long bookingId) {
        super(String.format("User %d is not authorized to access booking %d", userId, bookingId));
    }
}
