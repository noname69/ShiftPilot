package lt.techin.shiftpilot.feature.shiftassignment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignRequest;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignResponse;
import lt.techin.shiftpilot.feature.shiftassignment.service.ShiftAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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

}
