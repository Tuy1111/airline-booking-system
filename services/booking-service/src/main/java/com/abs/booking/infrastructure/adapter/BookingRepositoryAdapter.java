package com.abs.booking.infrastructure.adapter;

import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.repository.BookingRepository;
import com.abs.booking.domain.vo.BookingStatus;
import com.abs.booking.infrastructure.persistence.BookingJpaRepository;
import com.abs.booking.infrastructure.persistence.mapper.BookingPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class BookingRepositoryAdapter implements BookingRepository {
    private final BookingJpaRepository repository;

    @Override
    public BookingAggregate save(BookingAggregate aggregate) {
        return BookingPersistenceMapper.toAggregate(
                repository.save(BookingPersistenceMapper.toEntity(aggregate)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingAggregate> findById(Long id) {
        return repository.findById(id).map(BookingPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingAggregate> findByBookingCode(String bookingCode) {
        return repository.findByBookingCode(bookingCode).map(BookingPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingAggregate> findByUserId(Long userId, Pageable pageable) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(BookingPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingAggregate> findExpiredHolds(BookingStatus status, LocalDateTime now) {
        return repository.findExpiredHolds(status, now).stream()
                .map(BookingPersistenceMapper::toAggregate)
                .toList();
    }
}
