package lt.techin.shiftpilot.feature.managerapproval.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.ResourceNotFoundException;
import lt.techin.shiftpilot.exception.assignment.ApprovalException;
import lt.techin.shiftpilot.exception.assignment.AssignmentNotFoundException;
import lt.techin.shiftpilot.exception.core.BusinessException;
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
import lt.techin.shiftpilot.feature.managerapproval.repository.ManagerApprovalSpecifications;
import lt.techin.shiftpilot.feature.notification.model.NotificationType;
import lt.techin.shiftpilot.feature.notification.service.NotificationService;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.swaprequest.dto.TargetSwapResponseRequest;
import lt.techin.shiftpilot.feature.swaprequest.mapper.SwapRequestMapper;
import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequest;
import lt.techin.shiftpilot.feature.swaprequest.repository.SwapRequestRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final NotificationService notificationService;

    @Override
    public ManagerApprovalsList getAllManagerApprovals(Long managerId, ApprovalStatus status, LocalDate createdFrom, LocalDate createdTo, String requester, Pageable pageable) {

        LocalDateTime from = createdFrom != null
                ? createdFrom.atStartOfDay()
                : null;

        LocalDateTime to = createdTo != null
                ? createdTo.atTime(LocalTime.MAX)
                : null;

        Specification<ManagerApproval> spec =
                ManagerApprovalSpecifications.withFilters(
                        managerId,
                        null,
                        status,
                        from,
                        to,
                        requester
                );

        Page<ManagerApproval> page = managerApprovalRepository.findAll(spec, pageable);

        List<ManagerApprovalResponse> content = page.getContent()
                .stream()
                .map(approval -> {

                    ManagerApprovalResponse response = new ManagerApprovalResponse();

                    response.setApprovalId(approval.getId());
                    response.setApprovalStatus(approval.getStatus());
                    response.setType(approval.getType());
                    response.setCreatedAt(approval.getCreatedAt());
                    response.setManagerComment(approval.getManagerComment());

                    if (approval.getClosedAt() != null) {
                        response.setCompletedAt(approval.getClosedAt());
                    }

                    if (approval.getLeaveRequest() != null) {
                        response.setLeaveResponse(
                                leaveRequestMapper.leaveRequestToResponse(approval.getLeaveRequest())
                        );
                        response.setReason(approval.getLeaveRequest().getReason());
                        response.setRequestId(approval.getLeaveRequest().getId());
                    }

                    if (approval.getSwapRequest() != null) {
                        response.setSwapResponse(
                                swapRequestMapper.toResponse(approval.getSwapRequest())
                        );
                        response.setReason(approval.getSwapRequest().getReason());
                        response.setRequestId(approval.getSwapRequest().getId());
                    }

                    return response;
                })
                .toList();

        ManagerApprovalsList response = new ManagerApprovalsList();
        response.setContent(content);
        response.setNumber(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());

        return response;
    }

    @Override
    public ManagerApprovalsList getAllUserRequests(Long userId, ApprovalStatus status, LocalDate createdFrom, LocalDate createdTo, Pageable pageable) {

        LocalDateTime from = createdFrom != null
                ? createdFrom.atStartOfDay()
                : null;

        LocalDateTime to = createdTo != null
                ? createdTo.atTime(LocalTime.MAX)
                : null;

        Specification<ManagerApproval> spec =
                ManagerApprovalSpecifications.withFilters(
                        null,
                        userId,
                        status,
                        from,
                        to,
                        null
                );

        Page<ManagerApproval> page =
                managerApprovalRepository.findAll(spec, pageable);

        List<ManagerApprovalResponse> content = page.getContent()
                .stream()
                .map(approval -> {

                    ManagerApprovalResponse response = new ManagerApprovalResponse();

                    response.setApprovalId(approval.getId());
                    response.setApprovalStatus(approval.getStatus());
                    response.setType(approval.getType());
                    response.setCreatedAt(approval.getCreatedAt());
                    response.setManagerComment(approval.getManagerComment());

                    if (approval.getClosedAt() != null) {
                        response.setCompletedAt(approval.getClosedAt());
                    }

                    if (approval.getLeaveRequest() != null) {
                        response.setLeaveResponse(
                                leaveRequestMapper.leaveRequestToResponse(approval.getLeaveRequest())
                        );
                        response.setReason(approval.getLeaveRequest().getReason());
                        response.setRequestId(approval.getLeaveRequest().getId());
                    } else {
                        response.setSwapResponse(
                                swapRequestMapper.toResponse(approval.getSwapRequest())
                        );
                        response.setReason(approval.getSwapRequest().getReason());
                        response.setRequestId(approval.getSwapRequest().getId());
                    }

                    return response;
                })
                .toList();

        ManagerApprovalsList response = new ManagerApprovalsList();
        response.setContent(content);
        response.setNumber(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());

        return response;
    }

    @Transactional
    public void respondAsTarget(TargetSwapResponseRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        SwapRequest swapRequest = swapRequestRepository.findById(request.swapRequestId())
                .orElseThrow(() -> new AssignmentNotFoundException(request.swapRequestId()));

        ManagerApproval approval = swapRequest.getApproval();

        if (!swapRequest.getTargetUser().getId().equals(user.getId())) {
            throw new BusinessException("Not your swap request");
        }

        if (approval.getStatus() != ApprovalStatus.PENDING_TARGET_APPROVAL) {
             throw new BusinessException("Already processed");
        }

        if (!request.accepted()) {
            approval.setStatus(ApprovalStatus.TARGET_REJECTED);
            approval.setManagerComment(request.comment());
            approval.setClosedAt(LocalDateTime.now());

            String shift = swapRequest.getRequesterAssignment().getShift().getTitle() + ", " +
                    swapRequest.getRequesterAssignment().getShift().getShiftDate() + ", " +
                    swapRequest.getRequesterAssignment().getShift().getStartTime().toString().substring(0, 5) + " - " +
                    swapRequest.getRequesterAssignment().getShift().getEndTime().toString().substring(0, 5);

            notificationService.createNotification(
                    swapRequest.getRequester(),
                    "Swap request declined",
                    user.getFirstName() + " " + user.getLastName() + " declined your swap request for: " + shift,
                    NotificationType.REQUEST_REJECTED,
                    null);
            return;
        }

        approval.setStatus(ApprovalStatus.PENDING_MANAGER_APPROVAL);
        approval.setManagerComment(request.comment());

        User requester = swapRequest.getRequester();
        String message = String.format(
                "%s %s accepted %s %s's swap request.\n%s %s's shift: %s, %s, %s - %s\n%s %s's shift: %s, %s, %s - %s",
                user.getFirstName(), user.getLastName(),
                requester.getFirstName(), requester.getLastName(),
                user.getFirstName(), user.getLastName(),
                swapRequest.getTargetAssignment().getShift().getTitle(),
                swapRequest.getTargetAssignment().getShift().getShiftDate(),
                swapRequest.getTargetAssignment().getShift().getStartTime().toString().substring(0, 5),
                swapRequest.getTargetAssignment().getShift().getEndTime().toString().substring(0, 5),
                requester.getFirstName(), requester.getLastName(),
                swapRequest.getRequesterAssignment().getShift().getTitle(),
                swapRequest.getRequesterAssignment().getShift().getShiftDate(),
                swapRequest.getRequesterAssignment().getShift().getStartTime().toString().substring(0, 5),
                swapRequest.getRequesterAssignment().getShift().getEndTime().toString().substring(0, 5)
        );

        notificationService.createNotification(
                approval.getManager(),
                "Swap request awaiting your approval",
                message,
                NotificationType.REQUEST_SUBMITTED,
                swapRequest.getId()
        );
    }

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

            SwapRequest rejectedSwap = swapRequest;
            String rejectedShift = rejectedSwap.getTargetAssignment().getShift().getTitle() + ", " +
                    rejectedSwap.getTargetAssignment().getShift().getShiftDate() + ", " +
                    rejectedSwap.getTargetAssignment().getShift().getStartTime().toString().substring(0, 5) + " - " +
                    rejectedSwap.getTargetAssignment().getShift().getEndTime().toString().substring(0, 5);

            notificationService.createNotification(
                    rejectedSwap.getRequester(),
                    "Swap request rejected",
                    "Your swap request for shift: " + rejectedShift + " was rejected by the manager.",
                    NotificationType.REQUEST_REJECTED,
                    null);

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

        String newRequesterShift = String.format("%s, %s, %s - %s",
                targetAssignment.getShift().getTitle(),
                targetAssignment.getShift().getShiftDate(),
                targetAssignment.getShift().getStartTime().toString().substring(0, 5),
                targetAssignment.getShift().getEndTime().toString().substring(0, 5));

        String newTargetShift = String.format("%s, %s, %s - %s",
                requesterAssignment.getShift().getTitle(),
                requesterAssignment.getShift().getShiftDate(),
                requesterAssignment.getShift().getStartTime().toString().substring(0, 5),
                requesterAssignment.getShift().getEndTime().toString().substring(0, 5));

        notificationService.createNotification(
                requesterUser,
                "Swap request approved",
                "Your swap request was approved. You are now assigned to: " + newRequesterShift,
                NotificationType.REQUEST_APPROVED,
                null);

        notificationService.createNotification(
                targetUser,
                "Shift swap completed",
                "Your shift swap with " + requesterUser.getFirstName() + " " + requesterUser.getLastName() +
                        " was approved. You are now assigned to: " + newTargetShift,
                NotificationType.REQUEST_APPROVED,
                null);

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
            handleLeaveRequestRejection(leaveRequest);
            return new ManagerDecisionResponse("Request was rejected.");
        }

        removeFromShiftsWhenAbsence(leaveRequest.getOutFrom(), leaveRequest.getOutTill());

        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setClosedAt(LocalDateTime.now());
        managerApprovalRepository.save(approval);

        if (!LocalDate.now().isBefore(leaveRequest.getOutFrom())
                && !LocalDate.now().isAfter(leaveRequest.getOutTill())) {
            User requesterUser = leaveRequest.getRequester();
            requesterUser.setStatus(UserStatus.valueOf(request.getRequestType().name()));
            userRepository.save(requesterUser);
        }

        handleLeaveRequestApproval(leaveRequest);
        return new ManagerDecisionResponse("Request was approved.");
    }

    private void removeFromShiftsWhenAbsence(LocalDate from, LocalDate till) {

        List<ShiftAssignment> assignments = shiftAssignmentRepository.findAllInTimeFrame(from, till);

        assignments.forEach(assignment -> {
            assignment.setStatus(ShiftAssignmentStatus.REMOVED);
            assignment.setRemovedAt(LocalDateTime.now());
            shiftAssignmentRepository.save(assignment);
        });

    }

    private void handleLeaveRequestRejection(LeaveRequest leaveRequest) {

        String rejectedLeaveRequest = leaveRequest.getApproval().getType() + ", " +
                leaveRequest.getOutFrom() + ", " +
                leaveRequest.getOutTill();

        notificationService.createNotification(
                leaveRequest.getRequester(),
                "Leave request rejected",
                "Your leave request for type: " + rejectedLeaveRequest + " was rejected by the manager.",
                NotificationType.REQUEST_REJECTED,
                null);
    }

    private void handleLeaveRequestApproval(LeaveRequest leaveRequest) {

        String rejectedLeaveRequest = leaveRequest.getApproval().getType() + ", " +
                leaveRequest.getOutFrom() + ", " +
                leaveRequest.getOutTill();

        notificationService.createNotification(
                leaveRequest.getRequester(),
                "Leave request approved",
                "Your leave request for type: " + rejectedLeaveRequest + " was approved by the manager.",
                NotificationType.REQUEST_APPROVED,
                null);
    }




}
