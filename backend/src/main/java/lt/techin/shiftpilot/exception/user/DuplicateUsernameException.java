package lt.techin.shiftpilot.exception.user;

import lt.techin.shiftpilot.exception.core.DuplicateException;

public class DuplicateUsernameException extends DuplicateException {

    public DuplicateUsernameException(String username) {
        super("Username already exists: " + username);
    }
}
