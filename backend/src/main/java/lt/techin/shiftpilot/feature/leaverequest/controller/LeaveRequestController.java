package lt.techin.shiftpilot.feature.leaverequest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.leaverequest.dto.CreateLeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.dto.LeaveRequestResponse;
import lt.techin.shiftpilot.feature.leaverequest.service.LeaveRequestService;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/users/me/leave-requests")
    public ResponseEntity<LeaveRequestResponse> createLeaveRequest(Authentication authentication,
                                                                   @Valid @RequestBody CreateLeaveRequest request
    ) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found."));

        LeaveRequestResponse response = leaveRequestService.createLeaveRequest(user.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
