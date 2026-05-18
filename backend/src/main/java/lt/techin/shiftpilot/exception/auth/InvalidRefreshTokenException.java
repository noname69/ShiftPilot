package lt.techin.shiftpilot.exception.auth;

import lt.techin.shiftpilot.exception.core.TokenException;

public class InvalidRefreshTokenException extends TokenException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
