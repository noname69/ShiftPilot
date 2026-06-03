package lt.techin.shiftpilot.feature.shiftDraft.mapper;
import lt.techin.shiftpilot.feature.shiftDraft.dto.CreateDraftRequest;
import lt.techin.shiftpilot.feature.shiftDraft.dto.ShiftDraftResponse;
import lt.techin.shiftpilot.feature.shiftDraft.model.ShiftDraft;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShiftDraftMapper {

    public ShiftDraft toEntity(CreateDraftRequest request) {
        return ShiftDraft.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .minEmployees(request.getMinEmployees())
                .build();
    }

    public ShiftDraftResponse toResponse(ShiftDraft draft) {

        List<User> draftEmployees = draft.getDraftEmployees().stream()
                .map(draftEmployee -> draftEmployee.getDraftEmployee())
                .toList();

        return ShiftDraftResponse.builder()
                .id(draft.getId())
                .title(draft.getTitle())
                .description(draft.getDescription())
                .startTime(draft.getStartTime())
                .endTime(draft.getEndTime())
                .minEmployees(draft.getMinEmployees())
                .draftEmployees(draftEmployees)
                .build();
    }
}
