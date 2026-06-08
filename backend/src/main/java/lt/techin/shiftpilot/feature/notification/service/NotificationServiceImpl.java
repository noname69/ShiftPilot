package lt.techin.shiftpilot.feature.notification.service;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.notification.NotificationNotFoundException;
import lt.techin.shiftpilot.feature.notification.dto.NotificationResponse;
import lt.techin.shiftpilot.feature.notification.model.Notification;
import lt.techin.shiftpilot.feature.notification.model.NotificationType;
import lt.techin.shiftpilot.feature.notification.repository.NotificationRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public NotificationResponse createNotification(User recipient, String title, String message, NotificationType type) {
        return createNotification(recipient, title, message, type, null);
    }

    @Override
    public NotificationResponse createNotification(User recipient, String title, String message, NotificationType type, Long referenceId) {

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReferenceId(referenceId);

        Notification saved = notificationRepository.save(notification);

        return new NotificationResponse(saved.getId(), saved.getReferenceId(), title, message, type, saved.getCreatedAt(), saved.isRead());
    }

    @Override
    public List<NotificationResponse> getUserNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(n -> new NotificationResponse(n.getId(), n.getReferenceId(), n.getTitle(), n.getMessage(), n.getType(), n.getCreatedAt(), n.isRead()))
                .toList();
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
