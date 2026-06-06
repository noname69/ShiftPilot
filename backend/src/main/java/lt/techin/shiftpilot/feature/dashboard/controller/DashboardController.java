package lt.techin.shiftpilot.feature.dashboard.controller;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.dashboard.dto.EmployeeDashboardResponse;
import lt.techin.shiftpilot.feature.dashboard.dto.ManagerDashboardResponse;
import lt.techin.shiftpilot.feature.dashboard.service.DashboardService;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    @GetMapping("/manager")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ManagerDashboardResponse> getManagerDashboard(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam LocalDate weekStart,
            @RequestParam LocalDate weekEnd) {

        String username = jwt.getSubject();

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return ResponseEntity.ok(dashboardService.getManagerDashboard(manager.getId(), weekStart, weekEnd));
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeDashboardResponse> getEmployeeDashboard(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam LocalDate weekStart,
            @RequestParam LocalDate weekEnd) {

        String username = jwt.getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return ResponseEntity.ok(dashboardService.getEmployeeDashboard(user.getId(), weekStart, weekEnd));
    }
}
