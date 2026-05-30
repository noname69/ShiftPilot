package lt.techin.shiftpilot.feature.shiftassignment.dto;

import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record UserScheduleResponse(
        Long assignmentId,
        Long shiftId,
        String shiftTitle,
        LocalDate shiftDate,
        LocalTime shiftStartTime,
        LocalTime shiftEndTime,
        ShiftAssignmentStatus assignmentStatus
) {
}
