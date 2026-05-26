package lt.techin.shiftpilot.feature.swaprequest.dto;

//import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequestStatus;
import lt.techin.shiftpilot.feature.shift.dto.ShiftSummary;
import lt.techin.shiftpilot.feature.user.dto.UserSummary;

import java.time.LocalDateTime;

public record SwapRequestResponse(
        Long requestId,
//        Long approvalId,

        UserSummary requester,
        UserSummary targetUser,

        ShiftSummary requesterShift,
        ShiftSummary targetShift

//        SwapRequestStatus status,

//        String reason,
//
//        LocalDateTime createdAt,
//        LocalDateTime targetRespondedAt,
//        LocalDateTime managerRespondedAt,
//        LocalDateTime completedAt
) {
}
