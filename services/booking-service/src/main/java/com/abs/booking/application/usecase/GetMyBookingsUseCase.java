package com.abs.booking.application.usecase;

import com.abs.booking.application.dto.BookingDetailResponse;
import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetMyBookingsUseCase {

    private final BookingRepository bookingRepository;

    @Transactional(readOnly = true)
    public Page<BookingDetailResponse> execute(Long userId, String status, int page, int size) {
        Page<BookingAggregate> bookings = bookingRepository.findByUserId(
                userId, PageRequest.of(page, size));
        return bookings.map(BookingDetailResponse::of);
    }
}
