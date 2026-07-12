package com.abs.template.application;

import com.abs.template.domain.exception.BaggageNotFoundException;
import com.abs.template.domain.aggregate.BaggageAggregate;
import com.abs.template.domain.port.in.CheckInBaggageCommand;
import com.abs.template.domain.port.in.CheckInBaggageUseCase;
import com.abs.template.domain.port.in.GetBaggageUseCase;
import com.abs.template.domain.repository.BaggageRepository;
import com.abs.template.domain.port.out.DomainEventPublisher;
import com.abs.template.domain.vo.BaggageId;
import com.abs.template.domain.vo.Weight;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service — hiện thực các inbound port (use case).
 *
 * <p><b>Mỏng có chủ đích.</b> Nó KHÔNG chứa quy tắc nghiệp vụ (đã nằm trong aggregate);
 * nó chỉ <i>điều phối</i> một use case: dựng value object, gọi hành vi của aggregate,
 * lưu qua outbound port, rồi publish event. Ranh giới transaction nằm ở đây.
 *
 * <p>Chỉ phụ thuộc <i>interface</i> (port), không hề biết JPA hay Kafka — nên đổi hạ tầng
 * không đụng tới lõi.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BaggageApplicationService implements CheckInBaggageUseCase, GetBaggageUseCase {

    private final BaggageRepository repository;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public BaggageId checkIn(CheckInBaggageCommand command) {
        var weight = Weight.ofKg(command.weightKg());
        var allowance = Weight.ofKg(command.allowanceKg());

        // Quyết định nghiệp vụ (trong/ngoài hạn mức) thuộc về aggregate, không thuộc service.
        var baggage = BaggageAggregate.checkIn(
                BaggageId.newId(),
                command.bookingRef(),
                weight,
                allowance
        );

        repository.save(baggage);
        eventPublisher.publishAll(baggage.pullDomainEvents());

        log.info("Baggage checked in: id={} status={} weight={}kg",
                baggage.id(), baggage.status(), command.weightKg());
        return baggage.id();
    }

    @Override
    @Transactional(readOnly = true)
    public BaggageAggregate getById(BaggageId id) {
        return repository.findById(id)
                .orElseThrow(() -> new BaggageNotFoundException(id));
    }
}
