package lt.techin.shiftpilot.feature.shiftassignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import lt.techin.shiftpilot.feature.shiftassignment.dto.*;
import lt.techin.shiftpilot.feature.shiftassignment.service.ShiftAssignmentService;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShiftAssignmentController {

    private final ShiftAssignmentService shiftAssignmentService;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
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

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER', 'USER')")
    @GetMapping("/shifts/{shiftId}/shift-assignees")
    public ResponseEntity<ShiftAssignResponse> getShiftAssignees(@PathVariable Long shiftId){

        ShiftAssignResponse response = shiftAssignmentService.getShiftAssignees(shiftId);

        return ResponseEntity.ok().body(response);

    }
    
    @GetMapping("/users/me/shifts")
    public ResponseEntity<MyAssigneeResponseList> getUserShifts(@AuthenticationPrincipal Jwt jwt,
                                                                @RequestParam(required = false) LocalDate shiftDate,
                                                                @RequestParam(required = false) ShiftStatus shiftStatus,
                                                                @ParameterObject @PageableDefault(page = 0, size = 10, sort="assignedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        String username = jwt.getSubject();

        MyAssigneeResponseList responses = shiftAssignmentService.getUserShifts(username, shiftDate, shiftStatus, pageable);

        return ResponseEntity.ok().body(responses);

    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PatchMapping("/shifts/{shiftId}/shift-assignments/{userId}/remove")
    public ResponseEntity<AssigneeResponse> removeShiftAssignment(
            @PathVariable Long shiftId,
            @PathVariable Long userId) {

        AssigneeResponse response = shiftAssignmentService.removeShiftAssignment(shiftId, userId);

        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @GetMapping("/shift-assignments/schedule")
    public ResponseEntity<WeeklyScheduleResponse> getAllWeeklyUsersSchedule(
            @RequestParam(required = false) Long userId,
            @RequestParam LocalDate weekStart,
            @RequestParam LocalDate weekEnd) {

        WeeklyScheduleResponse scheduleResponse = shiftAssignmentService.getAllUsersScheduleByWeek(userId, weekStart, weekEnd);

        return ResponseEntity.ok(scheduleResponse);
    }

    
    @GetMapping("/shift-assignments/me/schedule")
    public ResponseEntity<WeeklyScheduleResponse> getWeeklyUserSchedule(
            @RequestParam LocalDate weekStart,
            @RequestParam LocalDate weekEnd,
            @AuthenticationPrincipal Jwt jwt) {

        String username = jwt.getSubject();

        WeeklyScheduleResponse scheduleResponse = shiftAssignmentService.getUserScheduleByWeek(username, weekStart, weekEnd);

        return ResponseEntity.ok(scheduleResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("users/{userId}/shifts/{shiftId}")
    public ResponseEntity<WeeklyScheduleResponse> removeEmployeeFromShift(@PathVariable Long userId,
                                                                        @PathVariable Long shiftId) {

        shiftAssignmentService.removeEmployeeFromShift(userId, shiftId);

        return ResponseEntity.noContent().build();
    }

}
