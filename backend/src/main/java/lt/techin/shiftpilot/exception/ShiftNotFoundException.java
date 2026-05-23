package lt.techin.shiftpilot.exception;

import lt.techin.shiftpilot.exception.core.NotFoundException;

public class ShiftNotFoundException extends NotFoundException {
    public ShiftNotFoundException(Long shiftId) {
        super("Shift not found with id: " + shiftId);
    }
}
