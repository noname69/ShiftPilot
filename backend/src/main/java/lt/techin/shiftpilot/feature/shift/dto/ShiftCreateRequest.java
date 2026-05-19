package lt.techin.shiftpilot.feature.shift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ShiftCreateRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull LocalDate shiftDate,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,
        @Min(1) int minEmployees
) {
}