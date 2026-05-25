package lt.techin.shiftpilot.feature.managerapproval.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManagerApprovalResponse {

    private Long approvalId;

//    private SwapRequest swapRequestId; // arba vienas arba kitas null;
    private Long leaveRequestId;

    private RequestType type;

    private ApprovalStatus approvalStatus;

    private String reason;

    private String managerComment;

    // sunku dabar isivaizduot ar dar reikia kazkokiu fieldsu, jei ka prisidek

}
