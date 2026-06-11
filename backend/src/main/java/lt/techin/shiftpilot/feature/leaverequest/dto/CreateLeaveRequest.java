package lt.techin.shiftpilot.feature.leaverequest.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateLeaveRequest {

    private String reason;

    @NotNull
    private RequestType type;

    @NotNull
    private LocalDate outFrom;

    @NotNull
    private LocalDate outTill;

    @NotNull
    private Long managerId;

    @AssertTrue(message = "Leaving date can't be in the past.")
    public boolean isOutFromPeriodValid() {
        if (outFrom == null) {
            return true;
        }

        return !outFrom.isBefore(LocalDate.now());
    }

    @AssertTrue(message = "Return date must be after start date.")
    public boolean isLeavePeriodValid() {
        if (outFrom == null || outTill == null) {
            return true;
        }
        return outTill.isAfter(outFrom);
    }





}
