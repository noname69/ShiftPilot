package lt.techin.shiftpilot.feature.dashboard.dto;

public record AttendanceEntry(
        Long userId,
        String firstName,
        String lastName,
        String status
) {
}
