package lt.techin.shiftpilot.exception.assignment;

import lt.techin.shiftpilot.exception.core.BusinessException;

public class PastShiftException extends BusinessException {
    public PastShiftException() {
        super("Cannot reschedule past shifts");
    }
}
