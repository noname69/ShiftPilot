package lt.techin.shiftpilot.feature.shiftDraft.dto;
import lombok.Builder;
import lombok.Getter;
import lt.techin.shiftpilot.feature.user.dto.UserResponse;

import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
public class ShiftDraftResponse {

    private Long id;

    private String title;
    private String description;

    private LocalTime startTime;
    private LocalTime endTime;
    private int minEmployees;

    private List<UserResponse> draftEmployees;

}
