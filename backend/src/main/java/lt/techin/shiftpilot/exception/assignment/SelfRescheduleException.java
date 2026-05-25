package lt.techin.shiftpilot.exception.assignment;

import lt.techin.shiftpilot.exception.core.BusinessException;

public class SelfRescheduleException extends BusinessException {
    public SelfRescheduleException() {
        super("Cannot reschedule with yourself");
    }
}
