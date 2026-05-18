package lt.techin.shiftpilot.feature.auth.dto;

import lt.techin.shiftpilot.feature.user.model.UserRole;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        UserRole role
) {
}
