package lt.techin.shiftpilot.feature.swaprequest.controller;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.swaprequest.dto.CreateRescheduleRequestDto;
import lt.techin.shiftpilot.feature.swaprequest.dto.RescheduleRequestResponseDto;
import lt.techin.shiftpilot.feature.swaprequest.service.RescheduleRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reschedule-requests")
@RequiredArgsConstructor
public class RescheduleRequestController {
    private final RescheduleRequestService rescheduleRequestService;

    @PostMapping
    public ResponseEntity<RescheduleRequestResponseDto> create(
            Authentication authentication,
            @RequestBody CreateRescheduleRequestDto request
    ) {

        String username = getUsername(authentication);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        rescheduleRequestService.createRequest(request, username)
                );
    }

    @GetMapping("/my")
    public ResponseEntity<List<RescheduleRequestResponseDto>> getAll(
            Authentication authentication
    ) {

        String username = getUsername(authentication);

        return ResponseEntity.ok(
                rescheduleRequestService.getMyRequests(username)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<List<RescheduleRequestResponseDto>> getAllRequests(
            Authentication authentication
    ) {
        String username = getUsername(authentication);

        return ResponseEntity.ok(
                rescheduleRequestService.getAllRequests()
        );
    }

    private String getUsername(Authentication authentication) {
        return authentication.getName();
    }
}
