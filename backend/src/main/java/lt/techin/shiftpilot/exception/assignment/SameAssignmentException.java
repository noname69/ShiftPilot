package lt.techin.shiftpilot.exception.assignment;

import lt.techin.shiftpilot.exception.core.BusinessException;

public class SameAssignmentException extends BusinessException {
    public SameAssignmentException() {
        super("Cannot reschedule same assignment");
    }
}
