package com.abs.flightsearch.api;

import com.abs.flightsearch.application.dto.AirportRequest;
import com.abs.flightsearch.application.usecase.CreateAirportUseCase;
import com.abs.flightsearch.application.usecase.GetAirportsUseCase;
import com.abs.flightsearch.application.usecase.UpdateAirportUseCase;
import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airports")
@RequiredArgsConstructor
public class AirportController {

    private final GetAirportsUseCase getAirportsUseCase;
    private final CreateAirportUseCase createAirportUseCase;
    private final UpdateAirportUseCase updateAirportUseCase;

    @GetMapping
    public ResponseEntity<List<AirportAggregate>> getAllAirports() {
        return ResponseEntity.ok(getAirportsUseCase.execute());
    }

    @PostMapping
    public ResponseEntity<AirportAggregate> createAirport(@Valid @RequestBody AirportRequest request) {
        AirportAggregate airport = createAirportUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(airport);
    }

    @PutMapping("/{code}")
    public ResponseEntity<AirportAggregate> updateAirport(
            @PathVariable("code") String code,
            @Valid @RequestBody AirportRequest request) {
        AirportAggregate airport = updateAirportUseCase.execute(code, request);
        return ResponseEntity.ok(airport);
    }
}
