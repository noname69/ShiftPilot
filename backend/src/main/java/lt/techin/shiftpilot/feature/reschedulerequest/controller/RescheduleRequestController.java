package lt.techin.shiftpilot.feature.reschedulerequest.controller;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.reschedulerequest.dto.CreateRescheduleRequestDto;
import lt.techin.shiftpilot.feature.reschedulerequest.dto.RescheduleRequestResponseDto;
import lt.techin.shiftpilot.feature.reschedulerequest.service.RescheduleRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reschedule-requests")
@RequiredArgsConstructor
public class RescheduleRequestController {
    private final RescheduleRequestService rescheduleRequestService;

    // 1. Create request
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

    private String getUsername(Authentication authentication) {
        return authentication.getName();
    }
}
