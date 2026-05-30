package lt.techin.shiftpilot.feature.managerapproval.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;

@Data
public class ManagerApprovalRequest {

    @NotNull
    private Long approvalId;

    @NotNull
    private Long requestId;

    @NotNull
    private RequestType requestType;

    @NotNull
    private boolean decision;

    private String comment;
}
