package com.abs.booking.application.usecase;

import com.abs.booking.application.dto.BookingDetailResponse;
import com.abs.booking.domain.vo.BookingStatus;
import com.abs.booking.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAllBookingsUseCase {

    private final BookingRepository bookingRepository;

    @Transactional(readOnly = true)
    public Page<BookingDetailResponse> execute(String keyword, BookingStatus status, int page, int size) {
        return bookingRepository.search(keyword, status, PageRequest.of(page, size))
                .map(BookingDetailResponse::of);
    }
}
