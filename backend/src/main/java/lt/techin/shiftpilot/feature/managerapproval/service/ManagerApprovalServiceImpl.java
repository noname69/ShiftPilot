package lt.techin.shiftpilot.feature.managerapproval.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.ResourceNotFoundException;
import lt.techin.shiftpilot.exception.assignment.ApprovalException;
import lt.techin.shiftpilot.exception.assignment.AssignmentNotFoundException;
import lt.techin.shiftpilot.exception.assignment.ShiftAssignmentException;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.leaverequest.mapper.LeaveRequestMapper;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.repository.LeaveRequestRepository;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalRequest;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalResponse;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalsList;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerDecisionResponse;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;
import lt.techin.shiftpilot.feature.managerapproval.repository.ManagerApprovalRepository;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.swaprequest.dto.ManagerSwapResponseRequest;
import lt.techin.shiftpilot.feature.swaprequest.dto.TargetSwapResponseRequest;
import lt.techin.shiftpilot.feature.swaprequest.mapper.SwapRequestMapper;
import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequest;
import lt.techin.shiftpilot.feature.swaprequest.repository.SwapRequestRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ManagerApprovalServiceImpl implements ManagerApprovalService{

    private final ManagerApprovalRepository managerApprovalRepository;
    private final SwapRequestMapper swapRequestMapper;
    private final LeaveRequestMapper leaveRequestMapper;
    private final UserRepository userRepository;
    private final SwapRequestRepository swapRequestRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final LeaveRequestRepository leaveRequestRepository;

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
                    else if (approval.getSwapRequest() != null) {
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

//    @Transactional
//    public void respondAsManager(ManagerSwapResponseRequest request, String username) {
//
//        User manager = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UserNotFoundException(username));
//
//        SwapRequest swapRequest = swapRequestRepository.findById(request.swapRequestId())
//                .orElseThrow(() -> new AssignmentNotFoundException(request.swapRequestId()));
//
//        ManagerApproval approval = swapRequest.getApproval();
//
//        // only assigned manager can approve
//        if (!approval.getManager().getId().equals(manager.getId())) {
//            System.out.println("Not your approval");
//            return;
//        }
//
//        // must be waiting for manager
//        if (approval.getStatus() != ApprovalStatus.PENDING_MANAGER_APPROVAL) {
//            System.out.println("Request already processed");
//            return;
//        }
//
//        // rejected by manager
//        if (!request.approved()) {
//            approval.setStatus(ApprovalStatus.MANAGER_REJECTED);
//            approval.setManagerComment(request.comment());
//            approval.setClosedAt(LocalDateTime.now());
//            return;
//        }
//
//
//        ShiftAssignment requesterAssignment = swapRequest.getRequesterAssignment();
//        ShiftAssignment targetAssignment = swapRequest.getTargetAssignment();
//
//        User requesterUser = requesterAssignment.getUser();
//        User targetUser = targetAssignment.getUser();
//
//        // swap users
//        requesterAssignment.setUser(targetUser);
//        targetAssignment.setUser(requesterUser);
//
//        // optional status updates
//        approval.setStatus(ApprovalStatus.APPROVED);
//        approval.setManagerComment(request.comment());
//        approval.setClosedAt(LocalDateTime.now());
//
//        // save assignments
//        shiftAssignmentRepository.save(requesterAssignment);
//        shiftAssignmentRepository.save(targetAssignment);
//
//        // save approval
//        managerApprovalRepository.save(approval);
//    }

    @Override
    @Transactional
    public ManagerDecisionResponse processRequest(String username, ManagerApprovalRequest request) {

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        ManagerApproval approval = managerApprovalRepository.findById(request.getApprovalId())
                .orElseThrow(() -> new ResourceNotFoundException("Approval", request.getApprovalId()));

        if(!(Objects.equals(approval.getManager().getId(), manager.getId()))) {
            throw new ApprovalException("Approval doesn't belong to manager with id: " + manager.getId());
        }


        approval.setClosedAt(LocalDateTime.now());
        if(StringUtils.hasText(request.getComment())) {
            approval.setManagerComment(request.getComment());
        }

        if(!request.getRequestType().equals(RequestType.SWAP)) {
            return handleLeaveRequest(request, approval, manager);
        }

        return respondAsManager(request, username);

    }

    public ManagerDecisionResponse respondAsManager(ManagerApprovalRequest request, String username) {

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        SwapRequest swapRequest = swapRequestRepository.findById(request.getRequestId())
                .orElseThrow(() -> new AssignmentNotFoundException(request.getRequestId()));

        ManagerApproval approval = swapRequest.getApproval();

        // only assigned manager can approve
        if (!approval.getManager().getId().equals(manager.getId())) {
            throw new ApprovalException("Approval doesn't belong to manager with id: " + manager.getId());
        }

        // must be waiting for manager
        if (approval.getStatus() != ApprovalStatus.PENDING_MANAGER_APPROVAL) {
            return new ManagerDecisionResponse("Request already processed");
        }

        // rejected by manager
        if (!request.isDecision()) {
            approval.setStatus(ApprovalStatus.MANAGER_REJECTED);
            approval.setManagerComment(request.getComment());
            approval.setClosedAt(LocalDateTime.now());
            return new ManagerDecisionResponse("Request was rejected.");
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
        approval.setManagerComment(request.getComment());
        approval.setClosedAt(LocalDateTime.now());

        // save assignments
        shiftAssignmentRepository.save(requesterAssignment);
        shiftAssignmentRepository.save(targetAssignment);

        // save approval
        managerApprovalRepository.save(approval);

        return new ManagerDecisionResponse("Request was approved.");
    }

    private ManagerDecisionResponse handleLeaveRequest(ManagerApprovalRequest request,
                                                       ManagerApproval approval,
                                                       User manager){

        LeaveRequest leaveRequest = leaveRequestRepository.findById(request.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Request" , request.getRequestId()));

        if (!approval.getManager().getId().equals(manager.getId())) {
            throw new ApprovalException("Approval doesn't belong to manager with id: " + manager.getId());
        }

        leaveRequest.setClosedAt(LocalDateTime.now());
        leaveRequestRepository.save(leaveRequest);

        if(!request.isDecision()) {
            approval.setStatus(ApprovalStatus.MANAGER_REJECTED);
            managerApprovalRepository.save(approval);
            return new ManagerDecisionResponse("Request was rejected.");
        }

        ShiftAssignment shiftAssignment = leaveRequest.getAssignment();
        shiftAssignment.setStatus(ShiftAssignmentStatus.REMOVED);
        shiftAssignment.setRemovedAt(LocalDateTime.now());
        shiftAssignmentRepository.save(shiftAssignment);

        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setClosedAt(LocalDateTime.now());
        managerApprovalRepository.save(approval);

        User requesterUser = leaveRequest.getRequester();
        requesterUser.setStatus(UserStatus.valueOf(request.getRequestType().toString()));
        requesterUser.setUpdatedAt(LocalDateTime.now());
        requesterUser.setOutFrom(leaveRequest.getOutFrom());
        requesterUser.setOutTill(leaveRequest.getOutTill());
        userRepository.save(requesterUser);

        return new ManagerDecisionResponse("Request was approved.");
    }


}
