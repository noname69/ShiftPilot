package lt.techin.shiftpilot.feature.shift.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.shift.dto.ShiftCreateRequest;
import lt.techin.shiftpilot.feature.shift.dto.ShiftResponse;
import lt.techin.shiftpilot.feature.shift.dto.ShiftUpdateRequest;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import lt.techin.shiftpilot.feature.shift.service.ShiftService;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    public ResponseEntity<ShiftResponse> createShift(
            @Valid @RequestBody ShiftCreateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getSubject();

        ShiftResponse response = shiftService.createShift(request, username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ShiftResponse>> getAllShifts(
            @RequestParam(required = false) ShiftStatus status,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) String createdBy,
            Pageable pageable
            ) {
        return ResponseEntity.ok(shiftService.getAllShifts(status, dateFrom, dateTo, createdBy, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftResponse> getShiftById(@PathVariable Long id) {
        return ResponseEntity.ok(shiftService.getShiftById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShiftResponse> updateShift(
            @PathVariable Long id,
            @Valid @RequestBody ShiftUpdateRequest request
    ) {
        return ResponseEntity.ok(shiftService.updateShift(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> cancelShift(@PathVariable Long id,
                                            @AuthenticationPrincipal Jwt jwt) {

        String username = jwt.getSubject();
        shiftService.cancelShift(id, username);

        return ResponseEntity.noContent().build();
    }
}