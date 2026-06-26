package com.abs.flightsearch.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlineAggregate {
    private String code;
    private String name;

    public static AirlineAggregate create(String code, String name) {
        if (code == null || code.length() != 2) {
            throw new IllegalArgumentException("Mã hãng hàng không phải đúng 2 ký tự");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên hãng hàng không không được trống");
        }
        return AirlineAggregate.builder()
                .code(code.toUpperCase())
                .name(name.trim())
                .build();
    }
}
