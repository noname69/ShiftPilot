package lt.techin.shiftpilot.feature.shiftassignment.dto;

import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveScheduleEntry(
        Long leaveRequestId,
        RequestType type,
        LocalDate outFrom,
        LocalDate outTill
) {
}
