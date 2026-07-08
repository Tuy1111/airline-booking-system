package com.abs.booking.domain.exception;

public class BookingNotFoundException extends BookingDomainException {
    public BookingNotFoundException(Long bookingId) {
        super("Không tìm thấy đặt vé với ID: " + bookingId);
    }
}
