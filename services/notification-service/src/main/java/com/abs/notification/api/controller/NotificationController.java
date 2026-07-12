package com.abs.notification.api.controller;

import com.abs.notification.api.dto.NotificationResponse;
import com.abs.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Chỉ phục vụ tra cứu lịch sử thông báo. Việc GỬI thông báo luôn đi qua Kafka
 * event ({@code booking.confirmed} / {@code booking.cancelled}) — không có endpoint
 * gửi trực tiếp qua HTTP.
 */
@RestController
@RequestMapping(ApiPath.NOTIFICATIONS)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository repo;

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

    @GetMapping(ApiPath.BY_USER)
    public Page<NotificationResponse> byUser(@PathVariable("userId") Long userId,
                                             @RequestParam(name = "page", defaultValue = "0") int page,
                                             @RequestParam(name = "size", defaultValue = "20") int size) {
        return repo.findByUserId(userId, PageRequest.of(page, size))
                .map(NotificationResponse::of);
    }
}
