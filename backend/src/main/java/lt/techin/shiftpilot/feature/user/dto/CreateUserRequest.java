package lt.techin.shiftpilot.feature.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;

public record CreateUserRequest(

        @NotBlank(message = "First name is required")
        @Size(min = 3, max = 50)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 3, max = 50)
        String lastName,

        @NotBlank(message = "Email is required")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "Email must be valid (example: user@domain.com)"
        )
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @NotNull(message = "Role is required")
        UserRole role

//        UserStatus status
        ) {
}
