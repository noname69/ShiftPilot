package lt.techin.shiftpilot.exception.user;


import lt.techin.shiftpilot.exception.core.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }

    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }
}
