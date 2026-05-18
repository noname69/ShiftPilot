package lt.techin.shiftpilot.exception.auth;

import lt.techin.shiftpilot.exception.core.TokenException;

public class InvalidTokenException extends TokenException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
