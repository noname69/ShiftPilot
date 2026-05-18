package lt.techin.shiftpilot.feature.user.dto;

import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        UserStatus status
) {
}
