package lt.techin.shiftpilot.exception.assignment;

import lt.techin.shiftpilot.exception.core.BusinessException;

public class TargetShiftStartedException extends BusinessException {
    public TargetShiftStartedException() {
        super("Target shift already started");
    }
}
