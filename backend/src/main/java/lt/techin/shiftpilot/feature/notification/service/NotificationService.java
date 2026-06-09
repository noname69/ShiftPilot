package lt.techin.shiftpilot.feature.notification.service;

import lt.techin.shiftpilot.feature.notification.dto.NotificationListResponse;
import lt.techin.shiftpilot.feature.notification.dto.NotificationResponse;
import lt.techin.shiftpilot.feature.notification.model.NotificationType;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(User user, String title, String message, NotificationType type);

    NotificationResponse createNotification(User user, String title, String message, NotificationType type, Long referenceId);

    NotificationListResponse getUserNotifications(String username, Pageable pageable);

    void markAsRead(Long notificationId);
}
