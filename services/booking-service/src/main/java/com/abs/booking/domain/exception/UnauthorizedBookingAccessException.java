package com.abs.booking.domain.exception;

public class UnauthorizedBookingAccessException extends BookingDomainException {
    public UnauthorizedBookingAccessException(Long userId, Long bookingId) {
        super(String.format("Người dùng %d không có quyền truy cập đặt vé %d", userId, bookingId));
    }
}
