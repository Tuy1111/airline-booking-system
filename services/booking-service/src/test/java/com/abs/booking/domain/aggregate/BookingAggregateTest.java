package com.abs.booking.domain.aggregate;

import com.abs.booking.domain.vo.BookingStatus;
import com.abs.booking.domain.exception.InvalidBookingStateException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingAggregateTest {

    @Test
    void shouldCreateBookingInHeldState() {
        BookingAggregate booking = BookingAggregate.createHold(
                "BK20260624TEST", 1L, 100L, BigDecimal.valueOf(150000), 10
        );

        assertNotNull(booking);
        assertEquals("BK20260624TEST", booking.getBookingCode());
        assertEquals(1L, booking.getUserId());
        assertEquals(100L, booking.getFlightId());
        assertEquals(BookingStatus.HELD, booking.getStatus());
        assertEquals(BigDecimal.valueOf(15000), booking.getExpiresAt().minusMinutes(10).compareTo(booking.getHeldAt()) <= 0 ? BigDecimal.valueOf(15000) : BigDecimal.valueOf(0)); // verify TTL logic roughly
        assertNotNull(booking.getExpiresAt());
    }

    @Test
    void shouldConfirmBookingWhenStateIsHeld() {
        BookingAggregate booking = BookingAggregate.createHold(
                "BK20260624TEST", 1L, 100L, BigDecimal.valueOf(150000), 10
        );

        booking.confirm("PAY-12345");

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals("PAY-12345", booking.getPaymentId());
        assertNotNull(booking.getConfirmedAt());
    }

    @Test
    void shouldThrowExceptionWhenConfirmingAlreadyConfirmedBooking() {
        BookingAggregate booking = BookingAggregate.createHold(
                "BK20260624TEST", 1L, 100L, BigDecimal.valueOf(150000), 10
        );
        booking.confirm("PAY-12345");

        assertThrows(InvalidBookingStateException.class, () -> booking.confirm("PAY-67890"));
    }

    @Test
    void shouldCancelBookingWhenHeldOrConfirmed() {
        BookingAggregate booking1 = BookingAggregate.createHold(
                "BK20260624TEST1", 1L, 100L, BigDecimal.valueOf(150000), 10
        );
        booking1.cancel();
        assertEquals(BookingStatus.CANCELLED, booking1.getStatus());
        assertNotNull(booking1.getCancelledAt());

        BookingAggregate booking2 = BookingAggregate.createHold(
                "BK20260624TEST2", 1L, 100L, BigDecimal.valueOf(150000), 10
        );
        booking2.confirm("PAY-123");
        booking2.cancel();
        assertEquals(BookingStatus.CANCELLED, booking2.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCancellingCancelledBooking() {
        BookingAggregate booking = BookingAggregate.createHold(
                "BK20260624TEST", 1L, 100L, BigDecimal.valueOf(150000), 10
        );
        booking.cancel();

        assertThrows(InvalidBookingStateException.class, booking::cancel);
    }

    @Test
    void shouldExpireBookingWhenHeld() {
        BookingAggregate booking = BookingAggregate.createHold(
                "BK20260624TEST", 1L, 100L, BigDecimal.valueOf(150000), 10
        );
        booking.expire();
        assertEquals(BookingStatus.EXPIRED, booking.getStatus());
    }

    @Test
    void shouldDetectIfBookingIsExpired() {
        BookingAggregate booking = BookingAggregate.createHold(
                "BK20260624TEST", 1L, 100L, BigDecimal.valueOf(150000), 10
        );

        // Not expired yet
        assertFalse(booking.isExpired());

        // Set expiresAt to the past
        booking.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        assertTrue(booking.isExpired());
    }
}
