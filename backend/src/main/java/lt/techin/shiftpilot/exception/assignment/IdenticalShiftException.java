package lt.techin.shiftpilot.exception.assignment;

import lt.techin.shiftpilot.exception.core.BusinessException;

public class IdenticalShiftException extends BusinessException {
    public IdenticalShiftException() {
        super("Cannot reschedule identical shifts");
    }
}
