package lt.techin.shiftpilot.feature.shiftassignment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.ResourceNotFoundException;
import lt.techin.shiftpilot.exception.ShiftAssignmentNotFoundException;
import lt.techin.shiftpilot.exception.ShiftNotFoundException;
import lt.techin.shiftpilot.exception.assignment.AssignmentNotFoundException;
import lt.techin.shiftpilot.exception.assignment.ShiftAssignmentException;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.repository.LeaveRequestRepository;
import lt.techin.shiftpilot.feature.notification.dto.NotificationResponse;
import lt.techin.shiftpilot.feature.notification.model.Notification;
import lt.techin.shiftpilot.feature.notification.model.NotificationType;
import lt.techin.shiftpilot.feature.notification.service.NotificationService;
import lt.techin.shiftpilot.feature.notification.service.NotificationServiceImpl;
import lt.techin.shiftpilot.feature.shift.mapper.ShiftMapper;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import lt.techin.shiftpilot.feature.shift.repository.ShiftRepository;
import lt.techin.shiftpilot.feature.shiftassignment.dto.*;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.user.mapper.UserMapper;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftAssignmentServiceImpl implements ShiftAssignmentService{

    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserMapper userMapper;
    private final ShiftMapper shiftMapper;
    private final NotificationService notificationService;

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
        Set<Long> overlappingUserIds = preventOverlappingShift(shift);

        for (Long userId : request.getUserIds()) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));



            if(shiftAssignmentRepository.existsByUserIdAndShiftIdAndStatus(user.getId(), shift.getId(), ShiftAssignmentStatus.ASSIGNED)){
                throw new ShiftAssignmentException("User with id: " + userId + " is already assigned to shift with id: " + shift.getId());
            }

            if(!user.getStatus().equals(UserStatus.ACTIVE)) {
                throw new ShiftAssignmentException("User status: " + user.getStatus().toString() + ". Only ACTIVE users can be assigned to shift.");
            }

            if(overlappingUserIds.contains(user.getId())) {
                throw new ShiftAssignmentException("User with id: " + user.getId() + " is already assigned to another shift at the same time.");
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

            NotificationResponse assignmentNotification = notificationService.createNotification(
                    user,
                    "Shift assignment",
                    "You have been assigned to shift: " + shift.getTitle() + " on " + shift.getShiftDate() + " from " + shift.getStartTime() + " to " + shift.getEndTime() + ".",
                    NotificationType.SHIFT_ASSIGNED
            );

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

        return new ShiftAssignResponse(assignedUsers, overlappingUserIds);

    }

    @Override
    public ShiftAssignResponse getShiftAssignees(Long shiftId) {

        List<ShiftAssignment> assignments =
                shiftAssignmentRepository.findByShiftIdAndStatus(shiftId, ShiftAssignmentStatus.ASSIGNED);

        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException(shiftId));

        Set<Long> overlappingUserIds = preventOverlappingShift(shift);

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

        return new ShiftAssignResponse(responses, overlappingUserIds);

    }

    @Override
    public List<MyAssigneeResponse> getUserShifts(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<ShiftAssignment> shiftAssignments = shiftAssignmentRepository.findByUserOrStatus(user);

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
                            assignment.getId(),
                            assignment.getStatus()
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

    @Override
    public WeeklyScheduleResponse getUserScheduleByWeek(String username, LocalDate weekStart, LocalDate weekEnd) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<ShiftAssignment> assignments = shiftAssignmentRepository.findByUserIdAndShiftDateBetween(user.getId(), weekStart, weekEnd);

        List<UserScheduleResponse> shifts = assignments.stream()
                .map(sa -> {
                    Shift shift = sa.getShift();
                    return new UserScheduleResponse(
                            sa.getId(),
                            shift.getId(),
                            shift.getTitle(),
                            shift.getShiftDate(),
                            shift.getStartTime(),
                            shift.getEndTime(),
                            sa.getStatus()
                    );
                })
                .toList();

        List<LeaveRequest> leaveRequests = leaveRequestRepository.findApprovedLeaveRequestsInRange(
                user.getId(),
                weekStart,
                weekEnd
        );

        List<LeaveScheduleEntry> leaveEntries = leaveRequests.stream()
                .map(lr -> new LeaveScheduleEntry(
                        lr.getId(),
                        lr.getApproval().getType(),
                        lr.getOutFrom(),
                        lr.getOutTill()
                ))
                .toList();

        return new WeeklyScheduleResponse(shifts, leaveEntries);
    }

    @Override
    public void removeEmployeeFromShift(Long userId, Long shiftId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException(shiftId));

        ShiftAssignment assignment = shiftAssignmentRepository.findByUserIdAndShiftId(userId, shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift assignment not found for user with id: " + userId));

        shiftAssignmentRepository.delete(assignment);
    }

    @Override
    public WeeklyScheduleResponse getAllUsersScheduleByWeek(LocalDate weekStart, LocalDate weekEnd) {

        List<ShiftAssignment> assignments = shiftAssignmentRepository.findByShiftDateBetween(weekStart, weekEnd);

        List<UserScheduleResponse> shifts = assignments.stream()
                .map(sa -> {
                    Shift shift = sa.getShift();
                    return new UserScheduleResponse(
                            sa.getId(),
                            shift.getId(),
                            shift.getTitle(),
                            shift.getShiftDate(),
                            shift.getStartTime(),
                            shift.getEndTime(),
                            sa.getStatus()
                    );
                })
                .toList();

        List<LeaveRequest> leaveRequests = leaveRequestRepository.findApprovedLeaveRequestsInRange(
                weekStart,
                weekEnd
        );

        List<LeaveScheduleEntry> leaveEntries = leaveRequests.stream()
                .map(lr -> new LeaveScheduleEntry(
                        lr.getId(),
                        lr.getApproval().getType(),
                        lr.getOutFrom(),
                        lr.getOutTill()
                ))
                .toList();

        return new WeeklyScheduleResponse(shifts, leaveEntries);
    }

    private Set<Long> preventOverlappingShift(Shift shift) {

        List<Shift> overlappingShifts = shiftRepository
                .findOverlappingShifts(shift.getShiftDate(), shift.getStartTime(), shift.getEndTime());

        return shiftAssignmentRepository
                .findUserIdsByShiftIds(
                        overlappingShifts.stream()
                                .map(sh -> sh.getId())
                                .toList()
                );

    }
}
