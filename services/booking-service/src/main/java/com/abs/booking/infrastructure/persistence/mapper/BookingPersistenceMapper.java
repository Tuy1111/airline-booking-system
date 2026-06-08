package com.abs.booking.infrastructure.persistence.mapper;

import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.aggregate.BookingItem;
import com.abs.booking.infrastructure.persistence.entity.BookingEntity;
import com.abs.booking.infrastructure.persistence.entity.BookingItemEntity;

public final class BookingPersistenceMapper {

    private BookingPersistenceMapper() {
    }

    public static BookingAggregate toAggregate(BookingEntity entity) {
        return BookingAggregate.builder()
                .id(entity.getId())
                .bookingCode(entity.getBookingCode())
                .userId(entity.getUserId())
                .flightId(entity.getFlightId())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .currency(entity.getCurrency())
                .heldAt(entity.getHeldAt())
                .expiresAt(entity.getExpiresAt())
                .confirmedAt(entity.getConfirmedAt())
                .cancelledAt(entity.getCancelledAt())
                .paymentId(entity.getPaymentId())
                .version(entity.getVersion())
                .createdAt(entity.getCreatedAt())
                .items(entity.getItems().stream().map(BookingPersistenceMapper::toItem).toList())
                .build();
    }

    public static BookingEntity toEntity(BookingAggregate aggregate) {
        BookingEntity entity = BookingEntity.builder()
                .id(aggregate.getId())
                .bookingCode(aggregate.getBookingCode())
                .userId(aggregate.getUserId())
                .flightId(aggregate.getFlightId())
                .status(aggregate.getStatus())
                .totalAmount(aggregate.getTotalAmount())
                .currency(aggregate.getCurrency())
                .heldAt(aggregate.getHeldAt())
                .expiresAt(aggregate.getExpiresAt())
                .confirmedAt(aggregate.getConfirmedAt())
                .cancelledAt(aggregate.getCancelledAt())
                .paymentId(aggregate.getPaymentId())
                .version(aggregate.getVersion())
                .createdAt(aggregate.getCreatedAt())
                .build();
        aggregate.getItems().stream()
                .map(BookingPersistenceMapper::toItemEntity)
                .forEach(entity::addItem);
        return entity;
    }

    private static BookingItem toItem(BookingItemEntity entity) {
        return BookingItem.builder()
                .id(entity.getId())
                .seatNo(entity.getSeatNo())
                .passengerName(entity.getPassengerName())
                .passengerPassport(entity.getPassengerPassport())
                .price(entity.getPrice())
                .build();
    }

    private static BookingItemEntity toItemEntity(BookingItem item) {
        return BookingItemEntity.builder()
                .id(item.getId())
                .seatNo(item.getSeatNo())
                .passengerName(item.getPassengerName())
                .passengerPassport(item.getPassengerPassport())
                .price(item.getPrice())
                .build();
    }
}
