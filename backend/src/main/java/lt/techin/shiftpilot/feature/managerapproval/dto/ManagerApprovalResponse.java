package lt.techin.shiftpilot.feature.managerapproval.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.techin.shiftpilot.feature.leaverequest.dto.LeaveRequestResponse;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;
import lt.techin.shiftpilot.feature.swaprequest.dto.SwapRequestResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManagerApprovalResponse {

    private Long approvalId;
    private Long requestId;

    private SwapRequestResponse swapResponse;
    private LeaveRequestResponse leaveResponse;

    private RequestType type;
    private ApprovalStatus approvalStatus;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    private String managerComment;


}
