package lt.techin.shiftpilot.feature.swaprequest.controller;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.swaprequest.dto.CreateSwapRequest;
import lt.techin.shiftpilot.feature.swaprequest.dto.SwapRequestResponse;
import lt.techin.shiftpilot.feature.swaprequest.service.SwapRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/swap-requests")
@RequiredArgsConstructor
public class SwapRequestController {
    private final SwapRequestService swapRequestService;

    @PostMapping
    public ResponseEntity<SwapRequestResponse> create(
            Authentication authentication,
            @RequestBody CreateSwapRequest request
    ) {

        String username = getUsername(authentication);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        swapRequestService.createRequest(request, username)
                );
    }

//    @GetMapping("/my")
//    public ResponseEntity<List<SwapRequestResponse>> getAll(
//            Authentication authentication
//    ) {
//
//        String username = getUsername(authentication);
//
//        return ResponseEntity.ok(
//                swapRequestService.getMyRequests(username)
//        );
//    }

    @GetMapping("/debug")
    public Object debug(Authentication authentication) {
        return authentication.getAuthorities();
    }

//    @PreAuthorize("hasRole('MANAGER')")
//    @GetMapping("/all")
//    public ResponseEntity<List<SwapRequestResponse>> getAllRequests(
//            Authentication authentication
//    ) {
//        String username = getUsername(authentication);
//
//        return ResponseEntity.ok(
//                swapRequestService.getAllRequests()
//        );
//    }

    private String getUsername(Authentication authentication) {
        return authentication.getName();
    }
}
