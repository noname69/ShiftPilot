package lt.techin.shiftpilot.feature.notification.controller;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.notification.dto.NotificationListResponse;
import lt.techin.shiftpilot.feature.notification.dto.NotificationResponse;
import lt.techin.shiftpilot.feature.notification.service.NotificationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/me")
    public ResponseEntity<NotificationListResponse> getUserNotifications(
            @AuthenticationPrincipal Jwt jwt,
            @ParameterObject @PageableDefault(page = 0, size = 5, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        String username = jwt.getSubject();

        return ResponseEntity.ok(notificationService.getUserNotifications(username, pageable));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long notificationId) {
        String username = jwt.getSubject();
        notificationService.markAsRead(username, notificationId);

        return ResponseEntity.noContent().build();
    }
}