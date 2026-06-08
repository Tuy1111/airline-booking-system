package com.abs.template.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository thao tác trên {@link BaggageJpaEntity}.
 *
 * <p>Đây là chi tiết hạ tầng — KHÔNG phải port của domain. Domain chỉ biết
 * {@code BaggageRepository}; {@link BaggagePersistenceAdapter} bắc cầu giữa hai bên.
 */
public interface SpringDataBaggageRepository extends JpaRepository<BaggageJpaEntity, String> {
}
