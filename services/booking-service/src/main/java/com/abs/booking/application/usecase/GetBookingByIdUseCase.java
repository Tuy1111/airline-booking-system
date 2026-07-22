package com.abs.booking.application.usecase;

import com.abs.booking.application.dto.BookingDetailResponse;
import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.exception.BookingNotFoundException;
import com.abs.booking.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetBookingByIdUseCase {

    private final BookingRepository bookingRepository;

    @Transactional(readOnly = true)
    public BookingDetailResponse execute(Long id) {
        BookingAggregate booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        return BookingDetailResponse.of(booking);
    }
}
