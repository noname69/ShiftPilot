package lt.techin.shiftpilot.feature.auth.dto;

import lt.techin.shiftpilot.feature.user.model.UserRole;

public record AuthResponse(
        String firstName,
        String lastName,
        String message,
        String username,
        String email,
        Long userId,
        UserRole role
) {
}
