package lt.techin.shiftpilot.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        int status,
        boolean success,
        String message,
        T data,
        String path,
        LocalDateTime timestamp
) {
}
