package lt.techin.shiftpilot.feature.shiftassignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.techin.shiftpilot.feature.user.dto.UserResponse;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ShiftAssignResponse {

    private List<UserResponse> assignees;

}
