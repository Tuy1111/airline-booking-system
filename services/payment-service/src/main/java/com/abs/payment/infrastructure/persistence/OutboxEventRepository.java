package com.abs.payment.infrastructure.persistence;

import com.abs.payment.infrastructure.persistence.outbox.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    /**
     * Lấy 1 batch event PENDING với {@code FOR UPDATE SKIP LOCKED} — nhiều instance
     * relay có thể chạy song song mà không tranh nhau cùng 1 row (instance khác bỏ
     * qua row đã bị khoá thay vì chờ/đổ lỗi). Native query vì JPQL không hỗ trợ
     * SKIP LOCKED. {@code status} truyền vào dạng String (cột lưu enum name).
     */
    @Query(value = """
           SELECT * FROM outbox_event
            WHERE status = :status
            ORDER BY created_at ASC
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
           """, nativeQuery = true)
    List<OutboxEvent> lockPending(@Param("status") String status, @Param("limit") int limit);
}
