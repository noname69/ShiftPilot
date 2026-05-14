package lt.techin.shiftpilot.feature.notification.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lt.techin.shiftpilot.feature.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipientUser;

    private String title;
    private String message;
    private NotificationType type;
    Boolean isRead;
    LocalDateTime createdAt;

    public Notification(Long id, User recipientUser, String title, String message, NotificationType type, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.recipientUser = recipientUser;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}
