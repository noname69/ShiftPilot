package lt.techin.shiftpilot.feature.shiftassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ShiftAssignRequest {

    private List<Long> userIds;
}
