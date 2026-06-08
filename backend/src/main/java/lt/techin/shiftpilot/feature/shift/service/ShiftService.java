package lt.techin.shiftpilot.feature.shift.service;

import lt.techin.shiftpilot.feature.shift.dto.ShiftCreateRequest;
import lt.techin.shiftpilot.feature.shift.dto.ShiftResponse;
import lt.techin.shiftpilot.feature.shift.dto.ShiftUpdateRequest;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ShiftService {

    ShiftResponse createShift(ShiftCreateRequest request, String username);

    Page<ShiftResponse> getAllShifts(ShiftStatus status, LocalDate dateFrom, LocalDate dateTo, String createdBy, Pageable pageable);

    ShiftResponse getShiftById(Long id);

    ShiftResponse updateShift(Long id, ShiftUpdateRequest request);

    void deleteShift(Long id, String username);

}