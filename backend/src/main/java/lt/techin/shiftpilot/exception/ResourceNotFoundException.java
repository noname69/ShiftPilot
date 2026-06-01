package lt.techin.shiftpilot.exception;

import lt.techin.shiftpilot.exception.core.NotFoundException;

public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(resourceName + " with id: " + resourceId + " not found.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
