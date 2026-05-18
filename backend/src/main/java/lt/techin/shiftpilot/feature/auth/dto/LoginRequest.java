package lt.techin.shiftpilot.feature.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
