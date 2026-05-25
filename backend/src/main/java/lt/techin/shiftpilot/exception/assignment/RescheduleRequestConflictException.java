package lt.techin.shiftpilot.exception.assignment;

import lt.techin.shiftpilot.exception.core.BusinessException;

public class RescheduleRequestConflictException extends BusinessException {
    public RescheduleRequestConflictException() {
        super("Active reschedule request already exists for this shift pair");
    }
}
