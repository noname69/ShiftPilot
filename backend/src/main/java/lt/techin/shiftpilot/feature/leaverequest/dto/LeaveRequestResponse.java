package lt.techin.shiftpilot.feature.leaverequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
//import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequestStatus;
import lt.techin.shiftpilot.feature.shift.dto.ShiftSummary;
import lt.techin.shiftpilot.feature.user.dto.UserSummary;

@Builder
@Getter
@AllArgsConstructor
public class LeaveRequestResponse {

    private Long requestId;
//    private Long approvalId;
    private UserSummary requester;
    private ShiftSummary requesterShift;
//    private LeaveRequestStatus status;

}
