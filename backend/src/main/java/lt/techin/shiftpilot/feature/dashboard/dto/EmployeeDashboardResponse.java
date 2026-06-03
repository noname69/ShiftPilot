package lt.techin.shiftpilot.feature.dashboard.dto;

import java.util.List;

public record EmployeeDashboardResponse(
        List<UpcomingShiftEntry> upcomingShifts,
        TypeCounts requestSummary
) {
}
