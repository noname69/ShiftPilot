package lt.techin.shiftpilot.feature.leaverequest.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;

import java.time.LocalDateTime;

@Getter
public class CreateLeaveRequest {

    private String reason;

    @NotNull
    private RequestType type;

    @NotNull
    private LocalDateTime outFrom;

    @NotNull
    private LocalDateTime outTill;

    @AssertTrue(message = "Leaving date must be before coming back date.")
    public boolean isLeavePeriodValid() {
        if (outFrom == null || outTill == null) {
            return true;
        }

        return outTill.isAfter(outFrom);
    }



}
