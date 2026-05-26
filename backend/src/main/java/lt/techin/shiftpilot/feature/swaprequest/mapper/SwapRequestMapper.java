package lt.techin.shiftpilot.feature.swaprequest.mapper;

import lt.techin.shiftpilot.feature.swaprequest.dto.SwapRequestResponse;
import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequest;
import lt.techin.shiftpilot.feature.shift.dto.ShiftSummary;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.user.dto.UserSummary;
import lt.techin.shiftpilot.feature.user.model.User;

public class SwapRequestMapper {

    public static SwapRequestResponse toResponse(
            SwapRequest request
    ) {

        return new SwapRequestResponse(

                request.getId(),

                toUserSummary(request.getRequester()),
                toUserSummary(request.getTargetUser()),

                toShiftSummary(request.getRequesterAssignment()),
                toShiftSummary(request.getTargetAssignment()),

                request.getStatus(),

                request.getReason(),

                request.getCreatedAt(),
                request.getTargetRespondedAt(),
                request.getManagerRespondedAt(),
                request.getCompletedAt()
        );
    }

    private static UserSummary toUserSummary(User user) {
        return new UserSummary(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    private static ShiftSummary toShiftSummary(
            ShiftAssignment assignment
    ) {

        Shift shift = assignment.getShift();

        return new ShiftSummary(
                assignment.getId(),
                shift.getId(),
                shift.getTitle(),
                shift.getShiftDate(),
                shift.getStartTime(),
                shift.getEndTime()
        );
    }
}
