package lt.techin.shiftpilot.feature.managerapproval.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalRequest;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalResponse;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalsList;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerDecisionResponse;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.service.ManagerApprovalService;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ManagerApprovalController {

    private final ManagerApprovalService managerApprovalService;
    private final UserRepository userRepository;

    @GetMapping("/managers/me/manager-approvals")
    public ResponseEntity<ManagerApprovalsList> getAllManagerApprovals(Authentication authentication,
                                                                       @RequestParam(required = false) ApprovalStatus status,
                                                                       @RequestParam(required = false) LocalDate createdFrom,
                                                                       @RequestParam(required = false) LocalDate createdTo,
                                                                       @RequestParam(required = false) String requester,
                                                                       @ParameterObject @PageableDefault(page = 0, size = 10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found."));

        ManagerApprovalsList response = managerApprovalService.getAllManagerApprovals(manager.getId(), status, createdFrom, createdTo, requester, pageable);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/users/me/my-requests")
    public ResponseEntity<ManagerApprovalsList> getUserRequests(Authentication authentication,
                                                                @RequestParam(required = false) ApprovalStatus status,
                                                                @RequestParam(required = false) LocalDate createdFrom,
                                                                @RequestParam(required = false) LocalDate createdTo,
                                                                @ParameterObject @PageableDefault(page = 0, size = 10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found."));

        ManagerApprovalsList response = managerApprovalService.getAllUserRequests(user.getId(), status, createdFrom, createdTo, pageable);

        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/managers/me/process-request")
    public ResponseEntity<ManagerDecisionResponse> processRequest(Authentication authentication,
                                                                  @Valid @RequestBody ManagerApprovalRequest request) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();

        ManagerDecisionResponse response = managerApprovalService.processRequest(username, request);

        return ResponseEntity.ok().body(response);
    }
}
