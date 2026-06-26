package com.abs.notification.api;

import com.abs.notification.application.port.in.SendEmailUseCase;
import com.abs.notification.application.dto.SendEmailCommand;
import com.abs.notification.domain.aggregate.Notification;
import com.abs.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository repo;
    private final SendEmailUseCase service;

    @GetMapping
    public Page<Notification> list(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size) {
        return repo.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getOne(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-user/{userId}")
    public Page<Notification> byUser(@PathVariable Long userId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size) {
        return repo.findByUserId(userId, PageRequest.of(page, size));
    }

    @PostMapping("/test-send")
    public Notification testSend(@RequestBody SendEmailCommand cmd) {
        return service.sendEmail(cmd);
    }
}
