package lt.techin.shiftpilot.feature.managerapproval.service;

import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalRequest;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalsList;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerDecisionResponse;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import org.springframework.data.domain.Pageable;
import lt.techin.shiftpilot.feature.swaprequest.dto.TargetSwapResponseRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ManagerApprovalService {
    ManagerApprovalsList getAllManagerApprovals(Long id, ApprovalStatus status, LocalDate createdFrom, LocalDate createdTo, String requester, Pageable pageable);

    ManagerApprovalsList getAllUserRequests(Long id, ApprovalStatus status, LocalDate createdFrom, LocalDate createdTo, Pageable pageable);
    void respondAsTarget(TargetSwapResponseRequest request, String username);
//    void respondAsManager(ManagerSwapResponseRequest request, String username);
    ManagerDecisionResponse respondAsManager(ManagerApprovalRequest request, String username);
    ManagerDecisionResponse processRequest(String username, ManagerApprovalRequest request);
}
