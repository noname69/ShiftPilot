package lt.techin.shiftpilot.feature.shiftDraft.service;

import lt.techin.shiftpilot.feature.shiftDraft.dto.CreateDraftRequest;
import lt.techin.shiftpilot.feature.shiftDraft.dto.ShiftDraftResponse;

import java.util.List;

public interface ShiftDraftService {
    List<ShiftDraftResponse> getAllDrafts();

    ShiftDraftResponse createDraft(CreateDraftRequest request, String username);

    void deleteDraft(Long draftId, String username);
}
