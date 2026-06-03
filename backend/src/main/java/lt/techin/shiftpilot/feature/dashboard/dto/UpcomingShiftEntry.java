package lt.techin.shiftpilot.feature.dashboard.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpcomingShiftEntry(
        Long shiftId,
        String title,
        LocalDate shiftDate,
        LocalTime startTime,
        LocalTime endTime
) {
}
