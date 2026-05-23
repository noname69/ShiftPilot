package lt.techin.shiftpilot.feature.shiftassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.user.model.UserRole;

@Getter
@Setter
@AllArgsConstructor
public class AssigneeResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private ShiftAssignmentStatus status;
}
