package lt.techin.shiftpilot.feature.swaprequest.dto;

public record CreateRescheduleRequestDto(
        Long requesterAssignmentId,
        Long targetAssignmentId,
        String reason
) {}
