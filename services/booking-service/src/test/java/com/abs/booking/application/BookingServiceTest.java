package com.abs.booking.application;

import com.abs.booking.application.dto.HoldSeatRequest;
import com.abs.booking.application.dto.HoldSeatResponse;
import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.repository.BookingRepository;
import com.abs.booking.domain.exception.SeatAlreadyHeldException;
import com.abs.booking.domain.exception.SeatNotAvailableException;
import com.abs.booking.infrastructure.client.FlightSearchClient;
import com.abs.booking.infrastructure.client.UserServiceClient;
import com.abs.booking.infrastructure.redis.SeatLockService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingEventPublisher eventPublisher;

    @Mock
    private FlightSearchClient flightSearchClient;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private SeatLockService seatLockService;

    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        meterRegistry = new SimpleMeterRegistry();
        bookingService = new BookingService(
                bookingRepository,
                eventPublisher,
                flightSearchClient,
                userServiceClient,
                seatLockService,
                meterRegistry,
                10
        );
    }

    @Test
    void shouldHoldSeatSuccessfully() {
        HoldSeatRequest request = new HoldSeatRequest(1L, "12A", "John Doe", "A1234567");
        Map<String, Object> seatInfo = new HashMap<>();
        seatInfo.put("status", "AVAILABLE");
        seatInfo.put("price", 150000.0);

        when(flightSearchClient.checkSeat(1L, "12A")).thenReturn(seatInfo);
        when(seatLockService.acquireLock(eq(1L), eq("12A"), eq(10L), any(Duration.class))).thenReturn(true);
        when(bookingRepository.save(any(BookingAggregate.class))).thenAnswer(invocation -> {
            BookingAggregate arg = invocation.getArgument(0);
            arg.setId(999L);
            return arg;
        });

        HoldSeatResponse response = bookingService.holdSeat(request, 10L);

        assertNotNull(response);
        assertEquals(999L, response.bookingId());
        assertEquals("12A", response.seatNo());
        assertEquals(BigDecimal.valueOf(150000.0), response.price());
        verify(eventPublisher, times(1)).publishHeld(any(BookingAggregate.class));
    }

    @Test
    void shouldThrowExceptionWhenSeatNotAvailable() {
        HoldSeatRequest request = new HoldSeatRequest(1L, "12A", "John Doe", "A1234567");
        when(flightSearchClient.checkSeat(1L, "12A")).thenReturn(null);

        assertThrows(SeatNotAvailableException.class, () -> bookingService.holdSeat(request, 10L));
        verify(seatLockService, never()).acquireLock(any(), any(), any(), any());
    }

    @Test
    void shouldThrowExceptionWhenSeatLockFails() {
        HoldSeatRequest request = new HoldSeatRequest(1L, "12A", "John Doe", "A1234567");
        Map<String, Object> seatInfo = new HashMap<>();
        seatInfo.put("status", "AVAILABLE");
        seatInfo.put("price", 150000.0);

        when(flightSearchClient.checkSeat(1L, "12A")).thenReturn(seatInfo);
        when(seatLockService.acquireLock(eq(1L), eq("12A"), eq(10L), any(Duration.class))).thenReturn(false);

        assertThrows(SeatAlreadyHeldException.class, () -> bookingService.holdSeat(request, 10L));
        verify(bookingRepository, never()).save(any());
    }
}
