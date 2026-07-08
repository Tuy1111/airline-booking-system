package com.abs.booking.domain.exception;

public class SeatNotAvailableException extends BookingDomainException {
    public SeatNotAvailableException(Long flightId, String seatNo) {
        super(String.format("Ghế %s trên chuyến bay %d không khả dụng", seatNo, flightId));
    }
}
