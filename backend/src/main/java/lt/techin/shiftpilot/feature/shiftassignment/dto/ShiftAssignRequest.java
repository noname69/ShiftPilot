package lt.techin.shiftpilot.feature.shiftassignment.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ShiftAssignRequest {

    private List<Long> userIds;
}
