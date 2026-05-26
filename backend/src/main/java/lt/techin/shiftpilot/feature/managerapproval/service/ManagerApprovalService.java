package lt.techin.shiftpilot.feature.managerapproval.service;

import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalsList;
import lt.techin.shiftpilot.feature.swaprequest.dto.ManagerSwapResponseRequest;
import lt.techin.shiftpilot.feature.swaprequest.dto.TargetSwapResponseRequest;

public interface ManagerApprovalService {
    ManagerApprovalsList getAllManagerApprovals(Long id);

    ManagerApprovalsList getAllUserRequests(Long id);

    void respondAsTarget(TargetSwapResponseRequest request, String username);
    void respondAsManager(ManagerSwapResponseRequest request, String username);

}
