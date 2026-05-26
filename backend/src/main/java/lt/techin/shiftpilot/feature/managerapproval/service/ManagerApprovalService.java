package lt.techin.shiftpilot.feature.managerapproval.service;

import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalsList;

public interface ManagerApprovalService {
    ManagerApprovalsList getAllManagerApprovals(Long id);

    ManagerApprovalsList getAllUserRequests(Long id);
}
