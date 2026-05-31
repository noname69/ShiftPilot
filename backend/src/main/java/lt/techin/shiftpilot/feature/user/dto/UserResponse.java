package lt.techin.shiftpilot.feature.user.dto;

import lombok.Builder;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        UserStatus status,
        LocalDate outFrom,
        LocalDate outTill
) {
}
