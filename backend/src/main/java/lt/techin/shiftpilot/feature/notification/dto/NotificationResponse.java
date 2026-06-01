package lt.techin.shiftpilot.feature.notification.dto;

import lt.techin.shiftpilot.feature.notification.model.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String title,
        String message,
        NotificationType type,
        LocalDateTime createdAt,
        boolean isRead
) {
}
