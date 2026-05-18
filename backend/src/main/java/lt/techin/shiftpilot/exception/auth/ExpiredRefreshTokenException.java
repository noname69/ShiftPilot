package lt.techin.shiftpilot.exception.auth;

import lt.techin.shiftpilot.exception.core.TokenException;

public class ExpiredRefreshTokenException extends TokenException {
    public ExpiredRefreshTokenException(String message) {
        super(message);
    }
}
