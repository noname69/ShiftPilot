package lt.techin.shiftpilot.feature.notification.repository;

import lt.techin.shiftpilot.feature.notification.model.Notification;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(Long recipientId);
}
