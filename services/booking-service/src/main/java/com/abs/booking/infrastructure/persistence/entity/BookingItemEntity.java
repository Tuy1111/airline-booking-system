package com.abs.booking.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_item")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookingItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id")
    private BookingEntity booking;

    @Column(name = "seat_no", nullable = false, length = 5)
    private String seatNo;

    @Column(name = "passenger_name", nullable = false, length = 100)
    private String passengerName;

    @Column(name = "passenger_passport", length = 20)
    private String passengerPassport;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
}
