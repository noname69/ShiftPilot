package lt.techin.shiftpilot.feature.user.dto;

import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;

public record UserFilter(
        String search,
        UserStatus status,
        UserRole role
) {
}
