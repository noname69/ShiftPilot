package lt.techin.shiftpilot.feature.leaverequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lt.techin.shiftpilot.feature.shift.dto.ShiftSummary;
import lt.techin.shiftpilot.feature.user.dto.UserSummary;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class LeaveRequestResponse {

    private UserSummary requester;
    private Long requestId;
    private LocalDate outFrom;
    private LocalDate outTill;
    private Long managerApprovalId;

}
