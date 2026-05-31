package lt.techin.shiftpilot.feature.leaverequest.mapper;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.leaverequest.dto.LeaveRequestResponse;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import lt.techin.shiftpilot.feature.shift.dto.ShiftSummary;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.user.dto.UserSummary;
import lt.techin.shiftpilot.feature.user.mapper.UserMapper;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeaveRequestMapper {

    private final UserMapper userMapper;

    public LeaveRequestResponse leaveRequestToResponse(LeaveRequest request){
        return LeaveRequestResponse.builder()
                .requester(toUserSummary(request.getRequester()))
                .requestId(request.getId())
                .outFrom(request.getOutFrom())
                .outTill(request.getOutTill())
                .managerApprovalId(request.getApproval().getId())
//                .requesterShift(toShiftSummary(request.getAssignment()))
                .build();
    }

    private static UserSummary toUserSummary(User user) {
        return new UserSummary(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

//    private static ShiftSummary toShiftSummary(
//            ShiftAssignment assignment
//    ) {
//
//        Shift shift = assignment.getShift();
//
//        return new ShiftSummary(
//                assignment.getId(),
//                shift.getId(),
//                shift.getTitle(),
//                shift.getShiftDate(),
//                shift.getStartTime(),
//                shift.getEndTime()
//        );
//    }
}
