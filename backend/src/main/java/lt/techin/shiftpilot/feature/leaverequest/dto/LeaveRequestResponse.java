package lt.techin.shiftpilot.feature.leaverequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequestStatus;

@Getter
@AllArgsConstructor
public class LeaveRequestResponse {

    private Long requestId;
    private Long approvalId;
    private Long requesterId;
    private Long assignmentId;
    private LeaveRequestStatus status;

}
