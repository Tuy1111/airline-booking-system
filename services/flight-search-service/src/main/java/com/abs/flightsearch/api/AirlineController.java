package com.abs.flightsearch.api;

import com.abs.flightsearch.application.dto.AirlineRequest;
import com.abs.flightsearch.application.usecase.CreateAirlineUseCase;
import com.abs.flightsearch.application.usecase.GetAirlinesUseCase;
import com.abs.flightsearch.domain.aggregate.AirlineAggregate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airlines")
@RequiredArgsConstructor
public class AirlineController {

    private final GetAirlinesUseCase getAirlinesUseCase;
    private final CreateAirlineUseCase createAirlineUseCase;

    @GetMapping
    public ResponseEntity<List<AirlineAggregate>> getAllAirlines() {
        return ResponseEntity.ok(getAirlinesUseCase.execute());
    }

    @PostMapping
    public ResponseEntity<AirlineAggregate> createAirline(@Valid @RequestBody AirlineRequest request) {
        AirlineAggregate airline = createAirlineUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(airline);
    }
}
