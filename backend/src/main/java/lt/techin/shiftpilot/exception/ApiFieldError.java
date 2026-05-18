package lt.techin.shiftpilot.exception;

public record ApiFieldError(
        String field,
        String message
) {
}
