package com.abs.template.adapter.in.web;

import com.abs.template.adapter.in.web.dto.BaggageResponse;
import com.abs.template.adapter.in.web.dto.CheckInBaggageRequest;
import com.abs.template.domain.vo.BaggageId;
import com.abs.template.domain.port.in.CheckInBaggageCommand;
import com.abs.template.domain.port.in.CheckInBaggageUseCase;
import com.abs.template.domain.port.in.GetBaggageUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Inbound Adapter (driving) — REST endpoint.
 *
 * <p>Chỉ phụ thuộc <i>inbound port</i> (use case interface), không phụ thuộc application
 * service cụ thể. Việc của nó: nhận HTTP → map request thành command → gọi port →
 * map kết quả thành response. Không có nghiệp vụ ở đây.
 */
@RestController
@RequestMapping("/api/baggage")
@RequiredArgsConstructor
public class BaggageController {

    private final CheckInBaggageUseCase checkInBaggage;
    private final GetBaggageUseCase getBaggage;

    @PostMapping
    public ResponseEntity<BaggageResponse> checkIn(@Valid @RequestBody CheckInBaggageRequest request,
                                                   UriComponentsBuilder uri) {
        BaggageId id = checkInBaggage.checkIn(new CheckInBaggageCommand(
                request.bookingRef(), request.weightKg(), request.allowanceKg()));

        var body = BaggageResponse.from(getBaggage.getById(id));
        URI location = uri.path("/api/baggage/{id}").build(id.value());
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public BaggageResponse get(@PathVariable String id) {
        return BaggageResponse.from(getBaggage.getById(BaggageId.of(id)));
    }
}
