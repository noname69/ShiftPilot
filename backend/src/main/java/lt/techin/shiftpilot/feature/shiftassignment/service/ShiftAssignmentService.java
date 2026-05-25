package lt.techin.shiftpilot.feature.shiftassignment.service;

import lt.techin.shiftpilot.feature.shift.dto.ShiftResponse;
<<<<<<< HEAD
import lt.techin.shiftpilot.feature.shiftassignment.dto.MyAssigneeResponse;
=======
import lt.techin.shiftpilot.feature.shiftassignment.dto.AssigneeResponse;
>>>>>>> origin/main
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignRequest;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignResponse;

import java.util.List;

public interface ShiftAssignmentService {
    ShiftAssignResponse assignShift(String username, ShiftAssignRequest request, Long shiftId);

    ShiftAssignResponse getShiftAssignees(Long shiftId);

<<<<<<< HEAD
    List<MyAssigneeResponse> getUserShifts(String username);
=======
    List<ShiftResponse> getUserShifts(String username);

    AssigneeResponse removeShiftAssignment(Long shiftId, Long userId);
>>>>>>> origin/main
}
