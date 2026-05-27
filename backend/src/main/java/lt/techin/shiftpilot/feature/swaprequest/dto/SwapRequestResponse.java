package lt.techin.shiftpilot.feature.swaprequest.dto;
import lt.techin.shiftpilot.feature.shift.dto.ShiftSummary;
import lt.techin.shiftpilot.feature.user.dto.UserSummary;

public record SwapRequestResponse(

        UserSummary requester,
        UserSummary targetUser,

        ShiftSummary requesterShift,
        ShiftSummary targetShift

) {
}
