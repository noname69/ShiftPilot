package lt.techin.shiftpilot.feature.swaprequest.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.assignment.*;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;
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

    @Override
    public SwapRequestResponse createRequest(CreateSwapRequest request, String requesterUsername) {

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new UserNotFoundException(requesterUsername));

        ShiftAssignment requesterAssignment = shiftAssignmentRepository.findById(request.requesterAssignmentId())
                .orElseThrow(() -> new AssignmentNotFoundException(request.requesterAssignmentId()));

        ShiftAssignment targetAssignment = shiftAssignmentRepository.findById(request.targetAssignmentId())
                .orElseThrow(() -> new AssignmentNotFoundException(request.targetAssignmentId()));

        validateCreateRequest(requester, requesterAssignment, targetAssignment);

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

        return swapRequestMapper.toResponse(saved);
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
