package lt.techin.shiftpilot.feature.leaverequest.mapper;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.leaverequest.dto.LeaveRequestResponse;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
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
}
