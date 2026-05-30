package lt.techin.shiftpilot.feature.managerapproval.service;

import jakarta.validation.Valid;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalRequest;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalsList;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerDecisionResponse;
import lt.techin.shiftpilot.feature.swaprequest.dto.ManagerSwapResponseRequest;
import lt.techin.shiftpilot.feature.swaprequest.dto.TargetSwapResponseRequest;

public interface ManagerApprovalService {
    ManagerApprovalsList getAllManagerApprovals(Long id);

    ManagerApprovalsList getAllUserRequests(Long id);

    void respondAsTarget(TargetSwapResponseRequest request, String username);
//    void respondAsManager(ManagerSwapResponseRequest request, String username);
    ManagerDecisionResponse respondAsManager(ManagerApprovalRequest request, String username);
    ManagerDecisionResponse processRequest(String username, ManagerApprovalRequest request);
}
