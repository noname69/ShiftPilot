package lt.techin.shiftpilot.feature.reschedulerequest.dto;

import lt.techin.shiftpilot.feature.reschedulerequest.model.RescheduleRequestStatus;

import java.time.LocalDateTime;

public record RescheduleRequestResponseDto(
        Long id,
        Long requesterId,
        Long targetUserId,
        Long requesterAssignmentId,
        Long targetAssignmentId,
        RescheduleRequestStatus status,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime targetRespondedAt,
        LocalDateTime managerRespondedAt,
        LocalDateTime completedAt
) {
}
