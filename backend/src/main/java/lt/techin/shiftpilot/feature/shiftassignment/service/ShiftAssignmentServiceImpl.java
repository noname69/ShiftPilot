package lt.techin.shiftpilot.feature.shiftassignment.service;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.ShiftNotFoundException;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.shift.dto.ShiftResponse;
import lt.techin.shiftpilot.feature.shift.mapper.ShiftMapper;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import lt.techin.shiftpilot.feature.shift.repository.ShiftRepository;
import lt.techin.shiftpilot.feature.shiftassignment.dto.AssigneeResponse;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignRequest;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignResponse;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.user.mapper.UserMapper;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftAssignmentServiceImpl implements ShiftAssignmentService{

    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;
    private final UserMapper userMapper;
    private final ShiftMapper shiftMapper;

    @Override
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

        assigneeIds.stream().forEach(id -> {

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));

            ShiftAssignment shiftAssignment = new ShiftAssignment();
            shiftAssignment.setShift(shift);
            shiftAssignment.setAssignedBy(manager);
            shiftAssignment.setUser(user);

            shiftAssignmentRepository.save(shiftAssignment);
            AssigneeResponse response = userMapper.toAssigneeResponse(user, ShiftAssignmentStatus.ASSIGNED);
            assignedUsers.add(response);
        });

        return new ShiftAssignResponse(assignedUsers);
    }

    @Override
    public ShiftAssignResponse getShiftAssignees(Long shiftId) {

        List<User> assignees = shiftAssignmentRepository.findUsersByShiftId(shiftId);

        List<AssigneeResponse> responses = assignees.stream()
                .map(user -> {
                    ShiftAssignmentStatus status = shiftAssignmentRepository.findStatusByUserIdAndShiftId(user.getId(), shiftId);
                    return userMapper.toAssigneeResponse(user, status);
                })
                .toList();

        return new ShiftAssignResponse(responses);
    }

    @Override
    public List<ShiftResponse> getUserShifts(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findByUser(user);

        return shiftAssignments.stream()
                .map(shiftAssignment -> shiftAssignment.getShift())
                .map(shiftMapper::toResponse)
                .toList();
    }
}
