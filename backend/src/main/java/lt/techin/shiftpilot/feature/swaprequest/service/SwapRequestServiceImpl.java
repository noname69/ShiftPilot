package lt.techin.shiftpilot.feature.swaprequest.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.assignment.*;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.managerapproval.dto.ManagerApprovalRequest;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;
import lt.techin.shiftpilot.feature.managerapproval.service.ManagerApprovalService;
import lt.techin.shiftpilot.feature.notification.dto.NotificationResponse;
import lt.techin.shiftpilot.feature.notification.model.NotificationType;
import lt.techin.shiftpilot.feature.notification.service.NotificationService;
import lt.techin.shiftpilot.feature.swaprequest.dto.CreateSwapRequest;
import lt.techin.shiftpilot.feature.swaprequest.dto.SwapRequestResponse;
import lt.techin.shiftpilot.feature.swaprequest.mapper.SwapRequestMapper;
import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequest;
import lt.techin.shiftpilot.feature.swaprequest.repository.SwapRequestRepository;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SwapRequestServiceImpl implements SwapRequestService {

    private final SwapRequestRepository swapRequestRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final UserRepository userRepository;
    private final SwapRequestMapper swapRequestMapper;
    private final NotificationService notificationService;
    private final ManagerApprovalService managerApprovalService;

    private static final List<ApprovalStatus> ACTIVE_STATUSES = List.of(
            ApprovalStatus.PENDING_TARGET_APPROVAL,
            ApprovalStatus.PENDING_MANAGER_APPROVAL
    );

    @Override
    public SwapRequestResponse createRequest(CreateSwapRequest request, String requesterUsername) {

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new UserNotFoundException(requesterUsername));

        ShiftAssignment requesterAssignment = shiftAssignmentRepository.findById(request.requesterAssignmentId())
                .orElseThrow(() -> new AssignmentNotFoundException(request.requesterAssignmentId()));

        ShiftAssignment targetAssignment = shiftAssignmentRepository.findById(request.targetAssignmentId())
                .orElseThrow(() -> new AssignmentNotFoundException(request.targetAssignmentId()));

        validateCreateRequest(requester, requesterAssignment, targetAssignment);

        boolean exists = swapRequestRepository
                .existsByRequesterAssignmentIdAndTargetAssignmentIdAndApproval_StatusIn(
                        request.requesterAssignmentId(),
                        request.targetAssignmentId(),
                        ACTIVE_STATUSES
                );

        if (exists) {
            throw new SameAssignmentException();
        }

        User manager = requesterAssignment.getAssignedBy();

        ManagerApproval approval = new ManagerApproval();
        approval.setManager(manager);
        approval.setType(RequestType.SWAP);
        approval.setStatus(ApprovalStatus.PENDING_TARGET_APPROVAL);

        SwapRequest req = SwapRequest.builder()
                .requester(requester)
                .targetUser(targetAssignment.getUser())
                .requesterAssignment(requesterAssignment)
                .targetAssignment(targetAssignment)
                .reason(request.reason())
                .approval(approval)
//                .status(SwapRequestStatus.PENDING_TARGET_APPROVAL)
                .build();

        SwapRequest saved = swapRequestRepository.save(req);

        String message = String.format(
                "%s %s wants to swap shifts with you.\nTheir shift: %s, %s, %s - %s\nYour shift: %s, %s, %s - %s",
                requester.getFirstName(), requester.getLastName(),
                requesterAssignment.getShift().getTitle(),
                requesterAssignment.getShift().getShiftDate(),
                requesterAssignment.getShift().getStartTime().toString().substring(0, 5),
                requesterAssignment.getShift().getEndTime().toString().substring(0, 5),
                targetAssignment.getShift().getTitle(),
                targetAssignment.getShift().getShiftDate(),
                targetAssignment.getShift().getStartTime().toString().substring(0, 5),
                targetAssignment.getShift().getEndTime().toString().substring(0, 5)
        );

        notificationService.createNotification(
                targetAssignment.getUser(),
                "Swap request from " + requester.getFirstName() + " " + requester.getLastName(),
                message,
                NotificationType.REQUEST_SUBMITTED,
                saved.getId()
        );

        return swapRequestMapper.toResponse(saved);
    }

    @Override
    public void managerRespondFromNotification(Long swapRequestId, boolean decision, String username) {
        SwapRequest swapRequest = swapRequestRepository.findById(swapRequestId)
                .orElseThrow(() -> new AssignmentNotFoundException(swapRequestId));

        ManagerApprovalRequest request = new ManagerApprovalRequest();
        request.setApprovalId(swapRequest.getApproval().getId());
        request.setRequestId(swapRequestId);
        request.setRequestType(RequestType.SWAP);
        request.setDecision(decision);

        managerApprovalService.processRequest(username, request);
    }

    private void validateCreateRequest(User requester,
                                       ShiftAssignment requesterAssignment,
                                       ShiftAssignment targetAssignment) {
        // Ownership check
        if (!requesterAssignment.getUser().getId().equals(requester.getId())) {
            throw new ShiftOwnershipException();
        }

        // Cannot swap with yourself
        if (requesterAssignment.getUser().getId()
                .equals(targetAssignment.getUser().getId())) {
            throw new SelfRescheduleException();
        }

        if (requesterAssignment.getId()
                .equals(targetAssignment.getId())) {
            throw new SameAssignmentException(            );
        }

        LocalDateTime requesterStart = getShiftStartDateTime(requesterAssignment);
        LocalDateTime targetStart = getShiftStartDateTime(targetAssignment);
        LocalDateTime now = LocalDateTime.now();

        // Prevent past shifts
        if (requesterStart.isBefore(now)) {
            throw new PastShiftException();
        }

        if (targetStart.isBefore(now)) {
            throw new TargetShiftStartedException();
        }

        // Same assignment check
        if (requesterAssignment.getId().equals(targetAssignment.getId())) {
            throw new SameAssignmentException();
        }

        if (requesterAssignment.getUser().getId()
                .equals(targetAssignment.getUser().getId())
                && requesterAssignment.getShift().getId()
                .equals(targetAssignment.getShift().getId())) {
            throw new IdenticalShiftException();
        }
    }

    private LocalDateTime getShiftStartDateTime(ShiftAssignment assignment) {
        return LocalDateTime.of(
                assignment.getShift().getShiftDate(),
                assignment.getShift().getStartTime()
        );
    }

//    @Override
//    @Transactional()
//    public List<SwapRequestResponse> getAllRequests() {
//
//        return swapRequestRepository
//                .findAllByOrderByCreatedAtDesc()
//                .stream()
//                .map(swapRequest -> swapRequestMapper.toResponse(swapRequest))
//                .toList();
//    }
//
//    @Override
//    @Transactional()
//
//    public List<SwapRequestResponse> getMyRequests(String username) {
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UserNotFoundException(username));
//
//        return swapRequestRepository
//                .findByRequesterIdOrTargetUserIdOrderByCreatedAtDesc(
//                        user.getId(),
//                        user.getId()
//                )
//                .stream()
//                .map(swapRequest -> swapRequestMapper.toResponse(swapRequest))
//                .toList();
//    }
}
