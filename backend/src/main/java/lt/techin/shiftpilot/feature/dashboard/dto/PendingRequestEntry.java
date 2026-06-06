package lt.techin.shiftpilot.feature.dashboard.dto;

import java.time.LocalDateTime;

public record PendingRequestEntry(
        Long approvalId,
        Long requestId,
        String type,
        String approvalStatus,
        String requesterFirstName,
        String requesterLastName,
        String targetFirstName,
        String targetLastName,
        LocalDateTime createdAt
) {
}
