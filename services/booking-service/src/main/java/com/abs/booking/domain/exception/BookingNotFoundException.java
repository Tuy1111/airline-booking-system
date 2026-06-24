package com.abs.booking.domain.exception;

public class BookingNotFoundException extends BookingDomainException {
    public BookingNotFoundException(Long id) {
        super(String.format("Booking not found with id: %d", id));
    }

    public BookingNotFoundException(String bookingCode) {
        super(String.format("Booking not found with code: %s", bookingCode));
    }
}
