package com.abs.booking.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingItem {
    private Long id;
    private String seatNo;
    private String passengerName;
    private String passengerPassport;
    private BigDecimal price;
}
