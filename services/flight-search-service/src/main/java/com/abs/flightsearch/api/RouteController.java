package com.abs.flightsearch.api;

import com.abs.flightsearch.application.dto.RouteRequest;
import com.abs.flightsearch.application.dto.RouteResponse;
import com.abs.flightsearch.application.usecase.CreateRouteUseCase;
import com.abs.flightsearch.application.usecase.GetRoutesUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final GetRoutesUseCase getRoutesUseCase;
    private final CreateRouteUseCase createRouteUseCase;

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAllRoutes() {
        return ResponseEntity.ok(getRoutesUseCase.execute());
    }

    @PostMapping
    public ResponseEntity<RouteResponse> createRoute(@Valid @RequestBody RouteRequest request) {
        RouteResponse route = createRouteUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(route);
    }
}
