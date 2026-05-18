package lt.techin.shiftpilot.exception;

import jakarta.servlet.http.HttpServletRequest;
import lt.techin.shiftpilot.exception.core.DuplicateException;
import lt.techin.shiftpilot.exception.core.NotFoundException;
import lt.techin.shiftpilot.exception.core.TokenException;
import lt.techin.shiftpilot.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(
            NotFoundException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(
                        HttpStatus.NOT_FOUND.value(),
//                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        false,
                        ex.getMessage(),
                        null,
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApiError> handleDuplicate(
            DuplicateException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> handleInvalidRefresh(TokenException ex) {
        return ResponseEntity.status(401).body(
                Map.of(
                        "error", "INVALID_REFRESH_TOKEN",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {

        return ResponseEntity.status(403).body(
                Map.of(
                        "status", 403,
                        "error", "FORBIDDEN",
                        "message", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleDisabledException(
            DisabledException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiError(
                        HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

//    @ExceptionHandler(DuplicateEmailException.class)
//    public ResponseEntity<ApiError> handleDuplicate(DuplicateEmailException ex, HttpServletRequest request) {
//        return ResponseEntity.status(HttpStatus.CONFLICT)
//                .body(new ApiError(
//                        HttpStatus.CONFLICT.value(),
//                        HttpStatus.CONFLICT.getReasonPhrase(),
//                        ex.getMessage(),
//                        request.getRequestURI(),
//                        LocalDateTime.now()
//                ));
//    }

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
