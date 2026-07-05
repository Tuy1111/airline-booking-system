package com.abs.booking.domain.exception;

public class InvalidBookingStateException extends BookingDomainException {
    public InvalidBookingStateException(String message) {
        super(message);
    }
}
