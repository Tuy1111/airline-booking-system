package com.abs.booking.infrastructure.persistence;

import com.abs.booking.infrastructure.persistence.entity.BookingEntity;
import com.abs.booking.domain.vo.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {

    Optional<BookingEntity> findByBookingCode(String bookingCode);

    Page<BookingEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<BookingEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<BookingEntity> findByStatusOrderByCreatedAtDesc(BookingStatus status, Pageable pageable);

    Page<BookingEntity> findByBookingCodeContainingIgnoreCaseOrderByCreatedAtDesc(String keyword, Pageable pageable);

    Page<BookingEntity> findByStatusAndBookingCodeContainingIgnoreCaseOrderByCreatedAtDesc(
            BookingStatus status, String keyword, Pageable pageable);

    @Query("SELECT b FROM BookingEntity b WHERE b.status = :status AND b.expiresAt < :now")
    List<BookingEntity> findExpiredHolds(@Param("status") BookingStatus status, @Param("now") LocalDateTime now);
}
