package lt.techin.shiftpilot.exception;

import jakarta.servlet.http.HttpServletRequest;
import lt.techin.shiftpilot.exception.core.NotFoundException;
import lt.techin.shiftpilot.exception.user.DuplicateEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            NotFoundException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiError> handleDuplicate(DuplicateEmailException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<ApiFieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream().map(error -> new ApiFieldError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ValidationError apiError = new ValidationError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                LocalDateTime.now(),
                errors
        );

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationError> handleJsonParseException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        Throwable rootCause = ex.getMostSpecificCause();

        String message = "Invalid field value";

        if (rootCause != null) {
            message = rootCause.getMessage();
        }

        List<ApiFieldError> errors = List.of(
                new ApiFieldError(
                        "body",
                        message
                )
        );

        ValidationError apiError = new ValidationError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Request parsing failed",
                request.getRequestURI(),
                LocalDateTime.now(),
                errors
        );

        return ResponseEntity.badRequest().body(apiError);
    }

}
