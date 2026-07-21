package com.abs.booking.domain.repository;

import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.vo.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    BookingAggregate save(BookingAggregate aggregate);
    Optional<BookingAggregate> findById(Long id);
    Optional<BookingAggregate> findByBookingCode(String bookingCode);
    Page<BookingAggregate> findByUserId(Long userId, Pageable pageable);
    Page<BookingAggregate> search(String keyword, BookingStatus status, Pageable pageable);
    List<BookingAggregate> findExpiredHolds(BookingStatus status, LocalDateTime now);
}
