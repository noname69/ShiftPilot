package lt.techin.shiftpilot.exception.assignment;

import lt.techin.shiftpilot.exception.core.NotFoundException;

public class AssignmentNotFoundException extends NotFoundException {
    public AssignmentNotFoundException(Long id) {
        super("Assignment not found with id: " + id);
    }
}
