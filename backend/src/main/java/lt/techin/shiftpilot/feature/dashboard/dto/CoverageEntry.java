package lt.techin.shiftpilot.feature.dashboard.dto;

public record CoverageEntry(
        Long assignedEmployees,
        Long minEmployees,
        Long understaffedShiftsCount
) {
}
