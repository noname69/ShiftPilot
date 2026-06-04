package lt.techin.shiftpilot.feature.shiftDraft.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.shiftDraft.dto.CreateDraftRequest;
import lt.techin.shiftpilot.feature.shiftDraft.dto.ShiftDraftResponse;
import lt.techin.shiftpilot.feature.shiftDraft.service.ShiftDraftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shift-drafts")
@RequiredArgsConstructor
public class ShiftDraftController {

    private final ShiftDraftService shiftDraftService;

    @GetMapping
    public ResponseEntity<List<ShiftDraftResponse>> getAllDrafts() {
        List<ShiftDraftResponse> response = shiftDraftService.getAllDrafts();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ShiftDraftResponse> createDraft(
            @Valid @RequestBody CreateDraftRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {

        String username = jwt.getSubject();
        ShiftDraftResponse response = shiftDraftService.createDraft(request, username);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/shift-drafts/{draftId}")
    public ResponseEntity<Void> deleteDraft(
            @PathVariable Long draftId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getSubject();

        shiftDraftService.deleteDraft(draftId, username);

        return ResponseEntity.noContent().build();
    }

}