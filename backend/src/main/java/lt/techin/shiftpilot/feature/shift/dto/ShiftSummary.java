package lt.techin.shiftpilot.feature.shift.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ShiftSummary(
        Long assignmentId,
        Long shiftId,
        String title,
        LocalDate shiftDate,
        LocalTime startTime,
        LocalTime endTime
) {
}
