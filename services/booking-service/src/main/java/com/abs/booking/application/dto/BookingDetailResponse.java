package com.abs.booking.application.dto;

import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.vo.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookingDetailResponse(
        Long id,
        String bookingCode,
        Long userId,
        Long flightId,
        BookingStatus status,
        BigDecimal totalAmount,
        Integer baggageWeightKg,
        BigDecimal baggageFee,
        String currency,
        LocalDateTime heldAt,
        LocalDateTime expiresAt,
        LocalDateTime confirmedAt,
        LocalDateTime cancelledAt,
        String paymentId,
        LocalDateTime createdAt,
        List<BookingItemDto> items
) {

    public record BookingItemDto(
            Long id,
            String seatNo,
            String passengerName,
            String passengerPassport,
            BigDecimal price
    ) {
    }

    public static BookingDetailResponse of(BookingAggregate b) {
        List<BookingItemDto> itemDtos = b.getItems() == null
                ? List.of()
                : b.getItems().stream()
                .map(item -> new BookingItemDto(
                        item.getId(),
                        item.getSeatNo(),
                        item.getPassengerName(),
                        item.getPassengerPassport(),
                        item.getPrice()
                ))
                .toList();

        return new BookingDetailResponse(
                b.getId(),
                b.getBookingCode(),
                b.getUserId(),
                b.getFlightId(),
                b.getStatus(),
                b.getTotalAmount(),
                b.getBaggageWeightKg(),
                b.getBaggageFee(),
                b.getCurrency(),
                b.getHeldAt(),
                b.getExpiresAt(),
                b.getConfirmedAt(),
                b.getCancelledAt(),
                b.getPaymentId(),
                b.getCreatedAt(),
                itemDtos
        );
    }
}

