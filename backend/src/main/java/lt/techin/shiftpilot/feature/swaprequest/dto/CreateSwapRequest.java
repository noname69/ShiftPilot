package lt.techin.shiftpilot.feature.swaprequest.dto;

public record CreateSwapRequest(
        Long requesterAssignmentId,
        Long targetAssignmentId,
        String reason
) {}
