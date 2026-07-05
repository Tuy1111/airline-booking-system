package com.abs.booking.domain.exception;

public class SeatAlreadyHeldException extends BookingDomainException {
    public SeatAlreadyHeldException(Long flightId, String seatNo) {
        super(String.format("Ghế %s trên chuyến bay %d đang được giữ bởi người khác", seatNo, flightId));
    }
}
