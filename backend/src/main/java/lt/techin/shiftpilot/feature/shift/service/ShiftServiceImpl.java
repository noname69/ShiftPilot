package lt.techin.shiftpilot.feature.shift.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.techin.shiftpilot.exception.core.BusinessException;
import lt.techin.shiftpilot.feature.shift.dto.ShiftCreateRequest;
import lt.techin.shiftpilot.feature.shift.dto.ShiftResponse;
import lt.techin.shiftpilot.feature.shift.dto.ShiftUpdateRequest;
import lt.techin.shiftpilot.feature.shift.mapper.ShiftMapper;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import lt.techin.shiftpilot.feature.shift.repository.ShiftRepository;
import lt.techin.shiftpilot.feature.shift.repository.ShiftSpecifications;
import lt.techin.shiftpilot.feature.shiftDraft.repository.DraftEmployeeRepository;
import lt.techin.shiftpilot.feature.shiftassignment.dto.ShiftAssignRequest;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.notification.model.NotificationType;
import lt.techin.shiftpilot.feature.notification.service.NotificationService;
import lt.techin.shiftpilot.feature.shiftassignment.service.ShiftAssignmentServiceImpl;
import lt.techin.shiftpilot.feature.swaprequest.repository.SwapRequestRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final UserRepository userRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final ShiftAssignmentServiceImpl shiftAssignmentService;
    private final DraftEmployeeRepository draftEmployeeRepository;
    private final NotificationService notificationService;
    private final SwapRequestRepository swapRequestRepository;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public ShiftResponse createShift(ShiftCreateRequest request, String username) {

        log.info("event=SHIFT_CREATE_REQUEST title={} shiftDate={} startTime={} endTime={} minEmployees={} createdByUsername={}",
                request.title(),
                request.shiftDate(),
                request.startTime(),
                request.endTime(),
                request.minEmployees(),
                username
        );

        validateShiftTime(request.startTime(), request.endTime());

        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("event=SHIFT_CREATOR_NOT_FOUND username={}", username);
                    return new IllegalArgumentException("Creator user not found: " + username);
                });

        Shift shift = shiftMapper.toEntity(request);
        shift.setCreatedBy(creator);
        shift.setStatus(calculateStatus(
                shift.getShiftDate(),
                shift.getStartTime(),
                shift.getEndTime()
        ));

        Shift savedShift = shiftRepository.save(shift);
        shiftRepository.flush();

        log.info("event=SHIFT_CREATED shiftId={} createdByUserId={} createdByUsername={}",
                savedShift.getId(),
                creator.getId(),
                creator.getUsername()
        );

        if(request.draftId() != null) {
            createShiftFromDraft(username, request.userIds(), savedShift.getId());
        }

        return shiftMapper.toResponse(savedShift);
    }

    @Override
    public Page<ShiftResponse> getAllShifts(ShiftStatus status, LocalDate dateFrom, LocalDate dateTo, String createdBy, Pageable pageable) {

        Specification<Shift> specification = ShiftSpecifications.withFilters(status, dateFrom, dateTo, createdBy);

        Page<Shift> shifts = shiftRepository.findAll(specification, pageable);
        return shifts.map(shift -> shiftMapper.toResponse(refreshStatusIfNeeded(shift)));
    }

    @Override
    public ShiftResponse getShiftById(Long id) {
        log.info("event=SHIFT_GET_REQUEST shiftId={}", id);

        Shift shift = findShiftById(id);
        Shift updatedShift = refreshStatusIfNeeded(shift);

        log.info("event=SHIFT_GET_SUCCESS shiftId={} status={}",
                updatedShift.getId(),
                updatedShift.getStatus()
        );

        return shiftMapper.toResponse(updatedShift);
    }

    @Override
    public ShiftResponse updateShift(Long id, ShiftUpdateRequest request) {
        log.info("event=SHIFT_UPDATE_REQUEST shiftId={} newTitle={} newDate={} newStartTime={} newEndTime={} newMinEmployees={}",
                id,
                request.title(),
                request.shiftDate(),
                request.startTime(),
                request.endTime(),
                request.minEmployees()
        );

        validateShiftTime(request.startTime(), request.endTime());

        Shift shift = findShiftById(id);

        ShiftStatus oldStatus = shift.getStatus();
        LocalDate oldDate = shift.getShiftDate();
        LocalTime oldStartTime = shift.getStartTime();
        LocalTime oldEndTime = shift.getEndTime();

        shift.setTitle(request.title());
        shift.setDescription(request.description());
        shift.setShiftDate(request.shiftDate());
        shift.setStartTime(request.startTime());
        shift.setEndTime(request.endTime());
        shift.setMinEmployees(request.minEmployees());
        if (shift.getStatus() != ShiftStatus.CANCELLED && shift.getStatus() != ShiftStatus.DELETED) {
            shift.setStatus(calculateStatus(
                    request.shiftDate(),
                    request.startTime(),
                    request.endTime()
            ));
        }

        Shift savedShift = shiftRepository.save(shift);

        log.info("event=SHIFT_UPDATED shiftId={} oldDate={} newDate={} oldStartTime={} newStartTime={} oldEndTime={} newEndTime={} oldStatus={} newStatus={} createdByUserId={}",
                savedShift.getId(),
                oldDate,
                savedShift.getShiftDate(),
                oldStartTime,
                savedShift.getStartTime(),
                oldEndTime,
                savedShift.getEndTime(),
                oldStatus,
                savedShift.getStatus(),
                savedShift.getCreatedBy() != null ? savedShift.getCreatedBy().getId() : null
        );

        return shiftMapper.toResponse(savedShift);
    }

    @Override
    public void deleteShift(Long id, String username) {
        log.info("event=SHIFT_DELETE_REQUEST shiftId={} requestedByUsername={}", id, username);

        Shift shift = findShiftById(id);

        if (shift.getStatus() != ShiftStatus.OPEN) {
            throw new IllegalStateException("Only OPEN shifts can be deleted.");
        }

        List<ShiftAssignment> assignments = shiftAssignmentRepository.findAllByShiftId(id);

        String dateStr = shift.getShiftDate().format(DATE_FMT);
        String timeStr = shift.getStartTime().format(TIME_FMT) + " - " + shift.getEndTime().format(TIME_FMT);

        assignments.stream()
                .filter(a -> a.getStatus() == ShiftAssignmentStatus.ASSIGNED)
                .forEach(a -> notificationService.createNotification(
                        a.getUser(),
                        "Removed from shift",
                        String.format("You have been removed from shift: %s, %s, %s",
                                shift.getTitle(), dateStr, timeStr),
                        NotificationType.SHIFT_CANCELLED
                ));

        List<ShiftAssignment> assignedAssignments = assignments.stream()
                .filter(a -> a.getStatus() == ShiftAssignmentStatus.ASSIGNED)
                .toList();

        assignedAssignments.forEach(a -> {
            a.setStatus(ShiftAssignmentStatus.REMOVED);
            a.setRemovedAt(java.time.LocalDateTime.now());
        });
        shiftAssignmentRepository.saveAll(assignedAssignments);

        if (!assignments.isEmpty()) {
            List<Long> assignmentIds = assignments.stream().map(ShiftAssignment::getId).toList();
            swapRequestRepository.findAllByAssignmentIds(assignmentIds).forEach(sr -> {
                if (sr.getApproval() != null) {
                    sr.getApproval().setStatus(ApprovalStatus.CANCELLED);
                }
            });
        }

        shift.setStatus(ShiftStatus.DELETED);
        shiftRepository.save(shift);

        log.info("event=SHIFT_DELETED shiftId={} title={} shiftDate={} deletedByUsername={}",
                id, shift.getTitle(), shift.getShiftDate(), username);
    }

    private Shift findShiftById(Long id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("event=SHIFT_NOT_FOUND shiftId={}", id);
                    return new IllegalArgumentException("Shift not found with id: " + id);
                });
    }

    private void validateShiftTime(LocalTime startTime, LocalTime endTime) {
        if (!endTime.isAfter(startTime)) {
            log.warn("event=INVALID_SHIFT_TIME startTime={} endTime={}", startTime, endTime);
            throw new IllegalArgumentException("End time must be later than start time.");
        }
    }

    private ShiftStatus calculateStatus(
            LocalDate shiftDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (shiftDate.isAfter(today)) {
            return ShiftStatus.OPEN;
        }

        if (shiftDate.isBefore(today)) {
            return ShiftStatus.COMPLETED;
        }

        if (now.isBefore(startTime)) {
            return ShiftStatus.OPEN;
        }

        if (!now.isBefore(startTime) && now.isBefore(endTime)) {
            return ShiftStatus.ONGOING;
        }

        return ShiftStatus.COMPLETED;
    }

    private Shift refreshStatusIfNeeded(Shift shift) {
        if (shift.getStatus() == ShiftStatus.CANCELLED || shift.getStatus() == ShiftStatus.DELETED) {
            return shift;
        }

        ShiftStatus calculatedStatus = calculateStatus(
                shift.getShiftDate(),
                shift.getStartTime(),
                shift.getEndTime()
        );

        if (shift.getStatus() != calculatedStatus) {
            ShiftStatus oldStatus = shift.getStatus();

            shift.setStatus(calculatedStatus);
            Shift savedShift = shiftRepository.save(shift);

            log.info("event=SHIFT_STATUS_CHANGED shiftId={} oldStatus={} newStatus={}",
                    savedShift.getId(),
                    oldStatus,
                    calculatedStatus
            );

            return savedShift;
        }

        return shift;
    }

    private void createShiftFromDraft(String username, List<Long> userIds, Long savedShiftId) {

        ShiftAssignRequest assignees = new ShiftAssignRequest(userIds);

        shiftAssignmentService.assignShift(username, assignees, savedShiftId);

    }

}