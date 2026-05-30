package lt.techin.shiftpilot.feature.shiftassignment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record UserScheduleResponse(
        Long shiftId,
        String shiftTitle,
        LocalDate shiftDate,
        LocalTime shiftStartTime,
        LocalTime shiftEndTime
) {
}
