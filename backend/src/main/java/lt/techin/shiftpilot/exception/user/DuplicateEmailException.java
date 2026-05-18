package lt.techin.shiftpilot.exception.user;

import lt.techin.shiftpilot.exception.core.DuplicateException;

public class DuplicateEmailException extends DuplicateException {
    public DuplicateEmailException(String email) {
        super("User with email '" + email + "' already exists.");
    }
}
