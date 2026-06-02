package lt.techin.shiftpilot.feature.notification.controller;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.notification.dto.NotificationResponse;
import lt.techin.shiftpilot.feature.notification.service.NotificationService;
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
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();

        return ResponseEntity.ok(notificationService.getUserNotifications(username));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);

        return ResponseEntity.noContent().build();
    }
}