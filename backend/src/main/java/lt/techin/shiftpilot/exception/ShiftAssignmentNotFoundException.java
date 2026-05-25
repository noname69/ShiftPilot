package lt.techin.shiftpilot.exception;

import lt.techin.shiftpilot.exception.core.NotFoundException;

public class ShiftAssignmentNotFoundException extends NotFoundException {
    public ShiftAssignmentNotFoundException(Long shiftId, Long userId) {
        super("Shift assignment not found for shift id: " + shiftId + " and user id: " + userId);
    }
}
