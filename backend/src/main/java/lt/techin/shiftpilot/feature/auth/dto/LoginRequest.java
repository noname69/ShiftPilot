package lt.techin.shiftpilot.feature.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank
        @Size(min = 3, message = "Username must be at least 3 characters long")
        String username,

        @NotBlank
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password
) {
}
