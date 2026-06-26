package com.abs.booking.domain.exception;

public class SeatAlreadyHeldException extends BookingDomainException {
    public SeatAlreadyHeldException(Long flightId, String seatNo) {
        super(String.format("Seat %s on flight %d is already held by another user", seatNo, flightId));
    }
}
