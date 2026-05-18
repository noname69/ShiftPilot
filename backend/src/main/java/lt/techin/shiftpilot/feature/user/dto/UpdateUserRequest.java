package lt.techin.shiftpilot.feature.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;

public record UpdateUserRequest(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String email,

        @NotNull
        UserRole role,

        @NotNull
        UserStatus status
) {
}
