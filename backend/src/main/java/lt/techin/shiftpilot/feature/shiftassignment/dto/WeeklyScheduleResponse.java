package lt.techin.shiftpilot.feature.shiftassignment.dto;

import java.util.List;

public record WeeklyScheduleResponse(
        List<UserScheduleResponse> shifts,
        List<LeaveScheduleEntry> leaveRequests
) {
}
