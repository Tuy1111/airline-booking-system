package com.abs.booking.infrastructure.persistence;

import com.abs.booking.infrastructure.persistence.entity.BookingItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingItemJpaRepository extends JpaRepository<BookingItemEntity, Long> {
}
