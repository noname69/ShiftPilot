package lt.techin.shiftpilot.feature.reschedulerequest.dto;

public record CreateRescheduleRequestDto(
        Long requesterAssignmentId,
        Long targetAssignmentId,
        String reason
) {}
