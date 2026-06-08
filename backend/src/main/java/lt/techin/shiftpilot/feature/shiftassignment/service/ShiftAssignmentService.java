package lt.techin.shiftpilot.feature.shiftassignment.service;

import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import lt.techin.shiftpilot.feature.shiftassignment.dto.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ShiftAssignmentService {
    ShiftAssignResponse assignShift(String username, ShiftAssignRequest request, Long shiftId);

    ShiftAssignResponse getShiftAssignees(Long shiftId);

    MyAssigneeResponseList getUserShifts(String username, LocalDate shiftDate, ShiftStatus shiftStatus, Pageable pageable);

    AssigneeResponse removeShiftAssignment(Long shiftId, Long userId);

    WeeklyScheduleResponse getUserScheduleByWeek(String username, LocalDate weekStart, LocalDate weekEnd);

    void removeEmployeeFromShift(Long userId, Long shiftId);

    WeeklyScheduleResponse getAllUsersScheduleByWeek(Long userId, LocalDate weekStart, LocalDate weekEnd);
}
