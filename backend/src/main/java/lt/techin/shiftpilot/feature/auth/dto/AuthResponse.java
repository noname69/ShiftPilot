package lt.techin.shiftpilot.feature.auth.dto;

import lt.techin.shiftpilot.feature.user.model.UserRole;

public record AuthResponse(
        String message,
        String username,
        Long userId,
        UserRole role
) {
}
