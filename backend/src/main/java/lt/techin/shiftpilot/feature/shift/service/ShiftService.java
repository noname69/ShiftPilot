package lt.techin.shiftpilot.feature.shift.service;

import lt.techin.shiftpilot.feature.shift.dto.ShiftCreateRequest;
import lt.techin.shiftpilot.feature.shift.dto.ShiftResponse;
import lt.techin.shiftpilot.feature.shift.dto.ShiftUpdateRequest;
import lt.techin.shiftpilot.security.principal.UserPrincipal;

import java.util.List;

public interface ShiftService {

    ShiftResponse createShift(ShiftCreateRequest request, String username);

    List<ShiftResponse> getAllShifts();

    ShiftResponse getShiftById(Long id);

    ShiftResponse updateShift(Long id, ShiftUpdateRequest request);

    void deleteShift(Long id);
}