package com.abs.payment.infrastructure.persistence;

import com.abs.payment.infrastructure.persistence.outbox.OutboxEvent;
import com.abs.payment.infrastructure.persistence.outbox.OutboxStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status, Pageable pageable);

    /** SELECT ... FOR UPDATE SKIP LOCKED — cho phép nhiều instance relay chạy song song. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@jakarta.persistence.QueryHint(name = "jakarta.persistence.lock.timeout", value = "0"))
    @Query("""
           SELECT e FROM OutboxEvent e
            WHERE e.status = :status
            ORDER BY e.createdAt ASC
           """)
    List<OutboxEvent> lockPending(@Param("status") OutboxStatus status, Pageable pageable);
}
