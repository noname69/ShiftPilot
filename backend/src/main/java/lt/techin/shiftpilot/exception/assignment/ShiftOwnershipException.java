package lt.techin.shiftpilot.exception.assignment;

import lt.techin.shiftpilot.exception.core.BusinessException;

public class ShiftOwnershipException extends BusinessException {
    public ShiftOwnershipException() {
        super("You can only reschedule your own shift");
    }
}
