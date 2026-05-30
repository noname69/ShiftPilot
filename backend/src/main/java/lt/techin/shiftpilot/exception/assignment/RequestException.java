package lt.techin.shiftpilot.exception.assignment;
import lt.techin.shiftpilot.exception.core.BusinessException;

public class RequestException extends BusinessException {
    public RequestException(String message) {
        super(message);
    }
}
