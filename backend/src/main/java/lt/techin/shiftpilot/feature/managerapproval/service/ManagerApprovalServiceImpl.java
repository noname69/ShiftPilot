package lt.techin.shiftpilot.feature.managerapproval.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.assignment.AssignmentNotFoundException;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.leaverequest.mapper.LeaveRequestMapper;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalResponse;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalsList;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.managerapproval.repository.ManagerApprovalRepository;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.swaprequest.dto.ManagerSwapResponseRequest;
import lt.techin.shiftpilot.feature.swaprequest.dto.TargetSwapResponseRequest;
import lt.techin.shiftpilot.feature.swaprequest.mapper.SwapRequestMapper;
import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequest;
import lt.techin.shiftpilot.feature.swaprequest.repository.SwapRequestRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerApprovalServiceImpl implements ManagerApprovalService{

    private final ManagerApprovalRepository managerApprovalRepository;
    private final SwapRequestMapper swapRequestMapper;
    private final LeaveRequestMapper leaveRequestMapper;
    private final UserRepository userRepository;
    private final SwapRequestRepository swapRequestRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;

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

    @Transactional
    public void respondAsTarget(TargetSwapResponseRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        SwapRequest swapRequest = swapRequestRepository.findById(request.swapRequestId())
                .orElseThrow(() -> new AssignmentNotFoundException(request.swapRequestId()));

        ManagerApproval approval = swapRequest.getApproval();

        if (!swapRequest.getTargetUser().getId().equals(user.getId())) {
            //throw new UnauthorizedException("Not your swap request");
            System.out.println("Not your swap request");
        }

        if (approval.getStatus() != ApprovalStatus.PENDING_TARGET_APPROVAL) {
            // throw new SwapRequestConflictException("Already processed");
            System.out.println("Already processed");
        }

        if (!request.accepted()) {
            approval.setStatus(ApprovalStatus.TARGET_REJECTED);
            approval.setManagerComment(request.comment());
            approval.setClosedAt(LocalDateTime.now());
            return;
        }

        approval.setStatus(ApprovalStatus.PENDING_MANAGER_APPROVAL);
        approval.setManagerComment(request.comment());
    }

    @Transactional
    public void respondAsManager(ManagerSwapResponseRequest request, String username) {

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        SwapRequest swapRequest = swapRequestRepository.findById(request.swapRequestId())
                .orElseThrow(() -> new AssignmentNotFoundException(request.swapRequestId()));

        ManagerApproval approval = swapRequest.getApproval();

        // only assigned manager can approve
        if (!approval.getManager().getId().equals(manager.getId())) {
            System.out.println("Not your approval");
            return;
        }

        // must be waiting for manager
        if (approval.getStatus() != ApprovalStatus.PENDING_MANAGER_APPROVAL) {
            System.out.println("Request already processed");
            return;
        }

        // rejected by manager
        if (!request.approved()) {
            approval.setStatus(ApprovalStatus.MANAGER_REJECTED);
            approval.setManagerComment(request.comment());
            approval.setClosedAt(LocalDateTime.now());
            return;
        }


        ShiftAssignment requesterAssignment = swapRequest.getRequesterAssignment();
        ShiftAssignment targetAssignment = swapRequest.getTargetAssignment();

        User requesterUser = requesterAssignment.getUser();
        User targetUser = targetAssignment.getUser();

        // swap users
        requesterAssignment.setUser(targetUser);
        targetAssignment.setUser(requesterUser);

        // optional status updates
        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setManagerComment(request.comment());
        approval.setClosedAt(LocalDateTime.now());

        // save assignments
        shiftAssignmentRepository.save(requesterAssignment);
        shiftAssignmentRepository.save(targetAssignment);

        // save approval
        managerApprovalRepository.save(approval);
    }
}
