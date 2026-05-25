package lt.techin.shiftpilot.feature.shiftassignment.dto;

import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record MyAssigneeResponse(
        Long id,
        String title,
        String description,
        LocalDate shiftDate,
        LocalTime startTime,
        LocalTime endTime,
        Integer minEmployees,
        ShiftStatus status,
        Long createdByUserId,
        String createdByUsername,
        Long assigneeId
) {
}
