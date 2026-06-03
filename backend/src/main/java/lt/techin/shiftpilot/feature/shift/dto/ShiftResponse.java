package lt.techin.shiftpilot.feature.shift.dto;

import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record ShiftResponse(
        Long id,
        String title,
        String description,
        LocalDate shiftDate,
        LocalTime startTime,
        LocalTime endTime,
        int minEmployees,
        ShiftStatus status,
        Long createdByUserId,
        String createdByUsername,
        String draftName
) {
}