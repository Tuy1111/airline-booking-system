package com.abs.notification.api.controller;

import com.abs.notification.api.dto.NotificationResponse;
import com.abs.notification.api.dto.SendNotificationRequest;
import com.abs.notification.application.usecase.SendDirectNotificationService;
import com.abs.notification.domain.repository.NotificationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Exposes notification history and a synchronous HTTP entry point for service-to-service messages.
 * Booking notifications continue to arrive asynchronously through Kafka.
 */
@RestController
@RequestMapping(ApiPath.NOTIFICATIONS)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository repo;
    private final SendDirectNotificationService directNotificationService;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> send(@Valid @RequestBody SendNotificationRequest request) {
        return ResponseEntity.ok(NotificationResponse.of(directNotificationService.send(
                request.userId(), request.title(), request.content(), request.type())));
    }

    @GetMapping
    public Page<NotificationResponse> list(@RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "20") int size) {
        return repo.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()))
                .map(NotificationResponse::of);
    }

    @GetMapping(ApiPath.BY_ID)
    public ResponseEntity<NotificationResponse> getOne(@PathVariable("id") Long id) {
        return repo.findById(id)
                .map(NotificationResponse::of)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping({ApiPath.BY_USER, ApiPath.BY_USER_LEGACY})
    public Page<NotificationResponse> byUser(@PathVariable("userId") Long userId,
                                             @RequestParam(name = "page", defaultValue = "0") int page,
                                             @RequestParam(name = "size", defaultValue = "20") int size) {
        return repo.findByUserId(userId, PageRequest.of(page, size))
                .map(NotificationResponse::of);
    }

    @GetMapping("/me")
    public Page<NotificationResponse> mine(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return repo.findByUserId(userId, PageRequest.of(page, size))
                .map(NotificationResponse::of);
    }
}
