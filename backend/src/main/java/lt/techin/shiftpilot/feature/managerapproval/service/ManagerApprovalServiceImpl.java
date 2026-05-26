package lt.techin.shiftpilot.feature.managerapproval.service;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.leaverequest.mapper.LeaveRequestMapper;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalResponse;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalsList;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.managerapproval.repository.ManagerApprovalRepository;
import lt.techin.shiftpilot.feature.swaprequest.mapper.SwapRequestMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerApprovalServiceImpl implements ManagerApprovalService{

    private final ManagerApprovalRepository managerApprovalRepository;
    private final SwapRequestMapper swapRequestMapper;
    private final LeaveRequestMapper leaveRequestMapper;

    @Override
    public ManagerApprovalsList getAllManagerApprovals(Long managerId) {

        List<ManagerApproval> approvalList =  managerApprovalRepository.findByManagerId(managerId);

        List<ManagerApprovalResponse> responseList = approvalList.stream()
                .map(approval -> {
                    ManagerApprovalResponse response = new ManagerApprovalResponse();
                    response.setApprovalId(approval.getId());

                    if(approval.getLeaveRequest() != null) {
                        response.setLeaveResponse(leaveRequestMapper.leaveRequestToResponse(approval.getLeaveRequest()));
                        response.setReason(approval.getLeaveRequest().getReason());
                        response.setRequestId(approval.getLeaveRequest().getId());
                    }
                    else {
                        response.setSwapResponse(swapRequestMapper.toResponse(approval.getSwapRequest()));
                        response.setReason(approval.getSwapRequest().getReason());
                        response.setRequestId(approval.getSwapRequest().getId());
                    }
                    response.setApprovalStatus(approval.getStatus());
                    response.setType(approval.getType());
                    response.setCreatedAt(approval.getCreatedAt());

                    if(approval.getClosedAt() != null) {
                        response.setCompletedAt(approval.getClosedAt());
                    }

                    if(StringUtils.hasText(approval.getManagerComment())){
                        response.setManagerComment(approval.getManagerComment());
                    };

                    return response;

                }).toList();

        return new ManagerApprovalsList(responseList);
    }

    @Override
    public ManagerApprovalsList getAllUserRequests(Long userId) {
        List<ManagerApproval> userRequests =  managerApprovalRepository.findAllByUserInvolved(userId);

        List<ManagerApprovalResponse> responseList = userRequests.stream()
                .map(approval -> {
                    ManagerApprovalResponse response = new ManagerApprovalResponse();
                    response.setApprovalId(approval.getId());

                    if(approval.getLeaveRequest() != null) {
                        response.setLeaveResponse(leaveRequestMapper.leaveRequestToResponse(approval.getLeaveRequest()));
                        response.setReason(approval.getLeaveRequest().getReason());
                        response.setRequestId(approval.getLeaveRequest().getId());
                    }
                    else {
                        response.setSwapResponse(swapRequestMapper.toResponse(approval.getSwapRequest()));
                        response.setReason(approval.getSwapRequest().getReason());
                        response.setRequestId(approval.getSwapRequest().getId());
                    }
                    response.setApprovalStatus(approval.getStatus());
                    response.setType(approval.getType());
                    response.setCreatedAt(approval.getCreatedAt());

                    if(approval.getClosedAt() != null) {
                        response.setCompletedAt(approval.getClosedAt());
                    }

                    if(StringUtils.hasText(approval.getManagerComment())){
                        response.setManagerComment(approval.getManagerComment());
                    };

                    return response;

                }).toList();

        return new ManagerApprovalsList(responseList);
    }
}
