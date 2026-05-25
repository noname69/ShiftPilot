package lt.techin.shiftpilot.exception.assignment;

import lt.techin.shiftpilot.exception.core.BusinessException;

public class SwapRequestConflictException extends BusinessException {
    public SwapRequestConflictException() {
        super("Active reschedule request already exists for this shift pair");
    }
}
