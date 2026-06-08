package com.abs.template.adapter.out.persistence;

import com.abs.template.domain.aggregate.BaggageAggregate;
import com.abs.template.domain.repository.BaggageRepository;
import com.abs.template.domain.vo.BaggageId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Outbound Adapter — hiện thực port {@link BaggageRepository} bằng JPA.
 *
 * <p>Đây là phía "driven" của Hexagonal: nó <i>phụ thuộc vào</i> domain (implements port),
 * chứ không ngược lại. Muốn đổi sang Mongo/Redis chỉ cần thay class này, lõi không hay biết.
 */
@Component
@RequiredArgsConstructor
public class BaggagePersistenceAdapter implements BaggageRepository {

    private final SpringDataBaggageRepository jpaRepository;

    @Override
    public BaggageAggregate save(BaggageAggregate baggage) {
        var saved = jpaRepository.save(BaggageMapper.toJpa(baggage));
        return BaggageMapper.toDomain(saved);
    }

    @Override
    public Optional<BaggageAggregate> findById(BaggageId id) {
        return jpaRepository.findById(id.value().toString())
                .map(BaggageMapper::toDomain);
    }
}
