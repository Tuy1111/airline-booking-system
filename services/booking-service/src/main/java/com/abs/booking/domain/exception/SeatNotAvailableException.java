package com.abs.booking.domain.exception;

public class SeatNotAvailableException extends BookingDomainException {
    public SeatNotAvailableException(Long flightId, String seatNo) {
        super(String.format("Seat %s on flight %d is not available", seatNo, flightId));
    }
}
