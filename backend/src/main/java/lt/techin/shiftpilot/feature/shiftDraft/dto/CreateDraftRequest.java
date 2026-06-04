package lt.techin.shiftpilot.feature.shiftDraft.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.Set;

@Getter
@AllArgsConstructor
public class CreateDraftRequest {

    @NotNull
    private String title;
    @NotNull
    private String description;

    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private int minEmployees;

    private Set<Long> userIds;

}
