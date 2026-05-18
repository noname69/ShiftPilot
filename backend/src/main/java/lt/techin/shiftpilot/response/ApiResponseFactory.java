package lt.techin.shiftpilot.response;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseFactory {

    public static <T> ApiResponse<T> success(
            T data,
            String message,
            HttpServletRequest request
    ) {
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                true,
                message,
                data,
                request.getRequestURI(),
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> created(
            T data,
            String message,
            HttpServletRequest request
    ) {
        return new ApiResponse<>(
                HttpStatus.CREATED.value(),
                true,
                message,
                data,
                request.getRequestURI(),
                LocalDateTime.now()
        );
    }
}
