package lt.techin.shiftpilot.feature.managerapproval.service;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalResponse;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalsList;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.managerapproval.repository.ManagerApprovalRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerApprovalServiceImpl implements ManagerApprovalService{

    private final ManagerApprovalRepository managerApprovalRepository;

    @Override
    public ManagerApprovalsList getAllManagerApprovals(Long managerId) {

        List<ManagerApproval> approvalList =  managerApprovalRepository.findByManagerId(managerId);

        List<ManagerApprovalResponse> responseList = approvalList.stream()
                .map(approval -> {
                    ManagerApprovalResponse response = new ManagerApprovalResponse();
                    response.setApprovalId(approval.getId());

                    if(approval.getLeaveRequest() != null) {
                        response.setLeaveRequestId(approval.getLeaveRequest().getId());
                        response.setReason(approval.getLeaveRequest().getReason());
                    }
//                    else {
//                        response.setSwapRequest(approval.getSwapRequest());
//                        response.setReason(approval.getSwapRequest().getReason());
//                    }
                    response.setApprovalStatus(approval.getStatus());
                    response.setType(approval.getType());
                    if(StringUtils.hasText(approval.getManagerComment())){
                        response.setManagerComment(approval.getManagerComment());
                    };
                    return response;
                }).toList();

        return new ManagerApprovalsList(responseList);
    }
}
