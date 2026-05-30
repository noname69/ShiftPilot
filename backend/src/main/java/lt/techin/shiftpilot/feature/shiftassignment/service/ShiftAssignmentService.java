package lt.techin.shiftpilot.feature.shiftassignment.service;

import lt.techin.shiftpilot.feature.shiftassignment.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface ShiftAssignmentService {
    ShiftAssignResponse assignShift(String username, ShiftAssignRequest request, Long shiftId);

    ShiftAssignResponse getShiftAssignees(Long shiftId);

    List<MyAssigneeResponse> getUserShifts(String username);

    AssigneeResponse removeShiftAssignment(Long shiftId, Long userId);

    WeeklyScheduleResponse getUserScheduleByWeek(String username, LocalDate weekStart, LocalDate weekEnd);

}
