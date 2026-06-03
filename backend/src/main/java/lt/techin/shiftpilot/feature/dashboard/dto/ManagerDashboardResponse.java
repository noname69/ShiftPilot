package lt.techin.shiftpilot.feature.dashboard.dto;

import java.util.List;

public record ManagerDashboardResponse(
        TypeCounts swapSummary,
        TypeCounts leaveSummary,
        List<PendingRequestEntry> pendingRequests,
        List<AttendanceEntry> todayAttendance
) {
}
