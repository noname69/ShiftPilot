package lt.techin.shiftpilot.exception.user;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("User with email '" + email + "' already exists.");
    }
}
