package com.abs.booking.infrastructure.persistence;

import com.abs.booking.domain.BookingItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingItemRepository extends JpaRepository<BookingItem, Long> {
}
