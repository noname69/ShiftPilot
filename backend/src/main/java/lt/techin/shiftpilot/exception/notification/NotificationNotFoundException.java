package lt.techin.shiftpilot.exception.notification;

import lt.techin.shiftpilot.exception.ResourceNotFoundException;

public class NotificationNotFoundException extends ResourceNotFoundException {
    public NotificationNotFoundException(Long id) {
        super("Notification not found by id: " + id);
    }
}
