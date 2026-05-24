package lt.techin.shiftpilot.feature.reschedulerequest.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.assignment.*;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.reschedulerequest.dto.CreateRescheduleRequestDto;
import lt.techin.shiftpilot.feature.reschedulerequest.dto.RescheduleRequestResponseDto;
import lt.techin.shiftpilot.feature.reschedulerequest.mapper.RescheduleRequestMapper;
import lt.techin.shiftpilot.feature.reschedulerequest.model.RescheduleRequest;
import lt.techin.shiftpilot.feature.reschedulerequest.model.RescheduleRequestStatus;
import lt.techin.shiftpilot.feature.reschedulerequest.repository.RescheduleRequestRepository;
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
public class RescheduleRequestServiceImpl implements RescheduleRequestService {

    private final RescheduleRequestRepository rescheduleRequestRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final UserRepository userRepository;

    @Override
    public RescheduleRequestResponseDto createRequest(CreateRescheduleRequestDto request, String requesterUsername) {

        System.out.println(request);
        System.out.println(requesterUsername);

        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new UserNotFoundException(requesterUsername));

        ShiftAssignment requesterAssignment = shiftAssignmentRepository.findById(request.requesterAssignmentId())
                .orElseThrow(() -> new AssignmentNotFoundException(request.requesterAssignmentId()));

        ShiftAssignment targetAssignment = shiftAssignmentRepository.findById(request.targetAssignmentId())
                .orElseThrow(() -> new AssignmentNotFoundException(request.targetAssignmentId()));

        validateCreateRequest(requester, requesterAssignment, targetAssignment);

        boolean exists = rescheduleRequestRepository
                .existsByRequesterAssignmentIdAndTargetAssignmentIdAndStatusIn(
                        requesterAssignment.getId(),
                        targetAssignment.getId(),
                        List.of(
                                RescheduleRequestStatus.PENDING_TARGET_APPROVAL,
                                RescheduleRequestStatus.PENDING_MANAGER_APPROVAL
                        )
                );

        if (exists) {
            throw new RescheduleRequestConflictException();
        }

        RescheduleRequest req = RescheduleRequest.builder()
                .requester(requester)
                .targetUser(targetAssignment.getUser())
                .requesterAssignment(requesterAssignment)
                .targetAssignment(targetAssignment)
                .reason(request.reason())
                .status(RescheduleRequestStatus.PENDING_TARGET_APPROVAL)
                .build();

        RescheduleRequest saved = rescheduleRequestRepository.save(req);

        return RescheduleRequestMapper.toResponse(saved);
    }

    private void validateCreateRequest(User requester,
                                       ShiftAssignment requesterAssignment,
                                       ShiftAssignment targetAssignment) {
        // 1. Ownership check
        if (!requesterAssignment.getUser().getId().equals(requester.getId())) {
            throw new ShiftOwnershipException();
        }

        // 2. Cannot swap with yourself
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

        // 3. Prevent past shifts

        if (requesterStart.isBefore(now)) {
            throw new PastShiftException();
        }

        if (targetStart.isBefore(now)) {
            throw new TargetShiftStartedException();
        }

        // 4. Same assignment check
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
}
