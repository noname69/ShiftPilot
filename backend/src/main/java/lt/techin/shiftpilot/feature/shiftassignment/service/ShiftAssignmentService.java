package lt.techin.shiftpilot.feature.shiftassignment.service;

import lt.techin.shiftpilot.feature.shift.dto.ShiftResponse;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignRequest;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignResponse;

import java.util.List;

public interface ShiftAssignmentService {
    ShiftAssignResponse assignShift(String username, ShiftAssignRequest request, Long shiftId);

    ShiftAssignResponse getShiftAssignees(Long shiftId);

    List<ShiftResponse> getUserShifts(String username);
}
