package com.abs.flightsearch.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportAggregate {
    private String iataCode;
    private String name;
    private String city;
    private String country;

    public static AirportAggregate create(String iataCode, String name, String city, String country) {
        if (iataCode == null || iataCode.length() != 3) {
            throw new IllegalArgumentException("Mã IATA của sân bay phải đúng 3 ký tự");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên sân bay không được trống");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("Thành phố không được trống");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Quốc gia không được trống");
        }
        return AirportAggregate.builder()
                .iataCode(iataCode.toUpperCase())
                .name(name.trim())
                .city(city.trim())
                .country(country.trim())
                .build();
    }
}
