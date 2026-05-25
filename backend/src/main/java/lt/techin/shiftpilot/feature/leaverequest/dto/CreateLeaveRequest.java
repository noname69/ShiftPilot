package lt.techin.shiftpilot.feature.leaverequest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;

@Getter
public class CreateLeaveRequest {

    private String reason;

    @NotNull
    private RequestType type; // tavo atveju is kart SWAP by default is fronto.

}
