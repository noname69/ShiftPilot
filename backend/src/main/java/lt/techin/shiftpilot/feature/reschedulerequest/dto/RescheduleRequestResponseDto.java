package lt.techin.shiftpilot.feature.reschedulerequest.dto;

import lt.techin.shiftpilot.feature.reschedulerequest.model.RescheduleRequestStatus;
import lt.techin.shiftpilot.feature.shift.dto.ShiftSummary;
import lt.techin.shiftpilot.feature.user.dto.UserSummary;

import java.time.LocalDateTime;

public record RescheduleRequestResponseDto(
        Long id,

        UserSummary requester,
        UserSummary targetUser,

        ShiftSummary requesterShift,
        ShiftSummary targetShift,

        RescheduleRequestStatus status,

        String reason,

        LocalDateTime createdAt,
        LocalDateTime targetRespondedAt,
        LocalDateTime managerRespondedAt,
        LocalDateTime completedAt
) {
}
