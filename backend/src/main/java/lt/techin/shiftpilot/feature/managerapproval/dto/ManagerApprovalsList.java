package lt.techin.shiftpilot.feature.managerapproval.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ManagerApprovalsList {

    private List<ManagerApprovalResponse> content;

}
