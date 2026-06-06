package lt.techin.shiftpilot.feature.shiftDraft.mapper;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.shiftDraft.dto.CreateDraftRequest;
import lt.techin.shiftpilot.feature.shiftDraft.dto.ShiftDraftResponse;
import lt.techin.shiftpilot.feature.shiftDraft.model.ShiftDraft;
import lt.techin.shiftpilot.feature.user.dto.UserResponse;
import lt.techin.shiftpilot.feature.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShiftDraftMapper {

    private final UserMapper userMapper;

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

        List<UserResponse> draftEmployees = draft.getDraftEmployees().stream()
                .map(draftEmployee -> userMapper.toResponse(draftEmployee.getDraftEmployee()))
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
