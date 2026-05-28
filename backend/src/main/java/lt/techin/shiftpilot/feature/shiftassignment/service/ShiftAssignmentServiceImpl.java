package lt.techin.shiftpilot.feature.shiftassignment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.ShiftAssignmentNotFoundException;
import lt.techin.shiftpilot.exception.ShiftNotFoundException;
import lt.techin.shiftpilot.exception.assignment.ShiftAssignmentException;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.shift.mapper.ShiftMapper;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import lt.techin.shiftpilot.feature.shift.repository.ShiftRepository;
import lt.techin.shiftpilot.feature.shiftassignment.dto.AssigneeResponse;
import lt.techin.shiftpilot.feature.shiftassignment.dto.MyAssigneeResponse;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignRequest;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignResponse;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.user.mapper.UserMapper;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShiftAssignmentServiceImpl implements ShiftAssignmentService{

    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;
    private final UserMapper userMapper;
    private final ShiftMapper shiftMapper;

    @Override
    @Transactional
    public ShiftAssignResponse assignShift(String username, ShiftAssignRequest request, Long shiftId) {

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException(shiftId));

        if (shift.getStatus() == ShiftStatus.CANCELLED) {
            throw new IllegalStateException("Cannot assign employees to a cancelled shift.");
        }

        List<Long> assigneeIds = request.getUserIds();
        List<AssigneeResponse> assignedUsers = new ArrayList<>();

        for (Long userId : request.getUserIds()) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));

            if(shiftAssignmentRepository.existsByUserIdAndShiftId(user.getId(), shift.getId())){
                throw new ShiftAssignmentException("User with id: " + userId + " is already assigned to shift with id: " + shift.getId());
            }

            Optional<ShiftAssignment> existing = shiftAssignmentRepository
                    .findByShiftIdAndUserId(shift.getId(), user.getId());

            ShiftAssignment shiftAssignment = existing.orElse(new ShiftAssignment());
            boolean isNew = existing.isEmpty();

            shiftAssignment.setShift(shift);
            shiftAssignment.setAssignedBy(manager);
            shiftAssignment.setUser(user);
            shiftAssignment.setStatus(ShiftAssignmentStatus.ASSIGNED);

            ShiftAssignment saved = shiftAssignmentRepository.save(shiftAssignment);

            AssigneeResponse response = new AssigneeResponse(
                    saved.getUser().getId(),
                    saved.getUser().getFirstName(),
                    saved.getUser().getLastName(),
                    saved.getUser().getEmail(),
                    saved.getUser().getRole(),
                    saved.getStatus(),
                    saved.getId()
            );
            shiftAssignment.setRemovedAt(null);
            shiftAssignment.setUpdatedAt(LocalDateTime.now());
            if (isNew) {
                shiftAssignment.setAssignedAt(LocalDateTime.now());
            }

            assignedUsers.add(response);
        }

        return new ShiftAssignResponse(assignedUsers);

    }

    @Override
    public ShiftAssignResponse getShiftAssignees(Long shiftId) {

        List<ShiftAssignment> assignments =
                shiftAssignmentRepository.findByShiftId(shiftId);

        List<AssigneeResponse> responses = assignments.stream()
                .map(a -> new AssigneeResponse(
                        a.getUser().getId(),
                        a.getUser().getFirstName(),
                        a.getUser().getLastName(),
                        a.getUser().getEmail(),
                        a.getUser().getRole(),
                        a.getStatus(),
                        a.getId()
                ))
                .toList();

        return new ShiftAssignResponse(responses);

    }

    @Override
    public List<MyAssigneeResponse> getUserShifts(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findByUser(user);

        return shiftAssignments.stream()
                .map(ShiftAssignment::getShift)
                .map(shift -> {
                    ShiftAssignment assignment = shiftAssignments.stream()
                            .filter(a -> a.getShift().getId().equals(shift.getId()))
                            .findFirst()
                            .orElseThrow();

                    return new MyAssigneeResponse(
                            shift.getId(),
                            shift.getTitle(),
                            shift.getDescription(),
                            shift.getShiftDate(),
                            shift.getStartTime(),
                            shift.getEndTime(),
                            shift.getMinEmployees(),
                            shift.getStatus(),
                            shift.getCreatedBy().getId(),
                            shift.getCreatedBy().getUsername(),
                            assignment.getId()
                    );
                })
                .toList();
    }

    @Override
    public AssigneeResponse removeShiftAssignment(Long shiftId, Long userId) {

        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException(shiftId));

        if (shift.getStatus() == ShiftStatus.CANCELLED) {
            throw new IllegalStateException("Cannot modify assignments of a cancelled shift.");
        }

        ShiftAssignment assignment = shiftAssignmentRepository.findAssignedByShiftIdAndUserId(shiftId, userId)
                .orElseThrow(() -> new ShiftAssignmentNotFoundException(shiftId, userId));

        assignment.setStatus(ShiftAssignmentStatus.REMOVED);
        assignment.setRemovedAt(LocalDateTime.now());
        assignment.setUpdatedAt(LocalDateTime.now());

        shiftAssignmentRepository.save(assignment);

        return userMapper.toAssigneeResponse(assignment.getUser(), ShiftAssignmentStatus.REMOVED, assignment.getUser().getId());
    }
}
