package lt.techin.shiftpilot.feature.shiftassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class ShiftAssignResponse {

    private List<AssigneeResponse> assignees;
    private Set<Long> overlappingUserIds;

}
