package lt.techin.shiftpilot.feature.managerapproval.controller;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalsList;
import lt.techin.shiftpilot.feature.managerapproval.service.ManagerApprovalService;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ManagerApprovalController {

    private final ManagerApprovalService managerApprovalService;
    private final UserRepository userRepository;

    @GetMapping("/managers/me/manager-approvals")
    public ResponseEntity<ManagerApprovalsList> getAllManagerApprovals(Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found."));

        ManagerApprovalsList response = managerApprovalService.getAllManagerApprovals(manager.getId());

        return ResponseEntity.ok().body(response);
    }
}
