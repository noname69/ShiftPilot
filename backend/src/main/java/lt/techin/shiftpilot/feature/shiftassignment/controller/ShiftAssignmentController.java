package lt.techin.shiftpilot.feature.shiftassignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.shiftassignment.dto.*;
import lt.techin.shiftpilot.feature.shiftassignment.service.ShiftAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShiftAssignmentController {

    private final ShiftAssignmentService shiftAssignmentService;

    @PostMapping("/shifts/{shiftId}/shift-assignments")
    public ResponseEntity<ShiftAssignResponse> assignShift(Authentication authentication,
                                                           @Valid @RequestBody ShiftAssignRequest request,
                                                           @PathVariable Long shiftId
                                                           ) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();

        ShiftAssignResponse response = shiftAssignmentService.assignShift(username, request, shiftId);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/shifts/{shiftId}/shift-assignees")
    public ResponseEntity<ShiftAssignResponse> getShiftAssignees(@PathVariable Long shiftId){

        ShiftAssignResponse response = shiftAssignmentService.getShiftAssignees(shiftId);

        return ResponseEntity.ok().body(response);

    }
    
    @GetMapping("/users/me/shifts")
    public ResponseEntity<List<MyAssigneeResponse>> getUserShifts(@AuthenticationPrincipal Jwt jwt) {

        String username = jwt.getSubject();

        List<MyAssigneeResponse> responses = shiftAssignmentService.getUserShifts(username);

        return ResponseEntity.ok().body(responses);

    }

    @PatchMapping("/shifts/{shiftId}/shift-assignments/{userId}/remove")
    public ResponseEntity<AssigneeResponse> removeShiftAssignment(
            @PathVariable Long shiftId,
            @PathVariable Long userId) {

        AssigneeResponse response = shiftAssignmentService.removeShiftAssignment(shiftId, userId);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/shift-assignments/me/schedule")
    public ResponseEntity<List<UserScheduleResponse>> getWeeklyUserSchedule(
            @RequestParam LocalDate weekStart,
            @RequestParam LocalDate weekEnd,
            @AuthenticationPrincipal Jwt jwt) {

        String username = jwt.getSubject();

        List<UserScheduleResponse> scheduleResponse = shiftAssignmentService.getUserScheduleByWeek(username, weekStart, weekEnd);

        return ResponseEntity.ok(scheduleResponse);
    }

}
