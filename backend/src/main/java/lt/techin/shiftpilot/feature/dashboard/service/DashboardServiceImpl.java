package lt.techin.shiftpilot.feature.dashboard.service;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.dashboard.dto.*;
import lt.techin.shiftpilot.feature.dashboard.projection.CoverageProjection;
import lt.techin.shiftpilot.feature.leaverequest.repository.LeaveRequestRepository;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;
import lt.techin.shiftpilot.feature.managerapproval.repository.ManagerApprovalRepository;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shift.repository.ShiftRepository;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ManagerApprovalRepository managerApprovalRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final ShiftRepository shiftRepository;

    @Override
    public ManagerDashboardResponse getManagerDashboard(Long managerId, LocalDate weekStart, LocalDate weekEnd) {

        LocalDateTime startOfWeek = weekStart.atStartOfDay();
        LocalDateTime endOfweek = weekEnd.atTime(LocalTime.MAX);

        List<ManagerApproval> approvals = managerApprovalRepository.findByManagerIdAndDateBetween(managerId, startOfWeek, endOfweek);

        List<ManagerApproval> swapApprovals = approvals.stream()
                .filter(a -> a.getType() == RequestType.SWAP)
                .toList();
        List<ManagerApproval> leaveApprovals = approvals.stream()
                .filter(a -> a.getType() != RequestType.SWAP)
                .toList();

        TypeCounts swapSummary = computeCounts(swapApprovals);
        TypeCounts leaveSummary = computeCounts(leaveApprovals);

        List<PendingRequestEntry> pendingRequests = approvals.stream()
                .filter(a -> a.getStatus() == ApprovalStatus.PENDING_TARGET_APPROVAL
                        || a.getStatus() == ApprovalStatus.PENDING_MANAGER_APPROVAL)
                .sorted(Comparator.comparingInt(ma ->
                        ma.getStatus() == ApprovalStatus.PENDING_MANAGER_APPROVAL ? 0 : 1))
                .map(this::toPendingRequestEntry)
                .toList();

        LocalDate today = LocalDate.now();

        Map<Long, AttendanceEntry> attendanceMap = new LinkedHashMap<>();

        shiftAssignmentRepository.findAssignedTodayByManagerId(today, managerId)
                .forEach(sa -> {
                    User u = sa.getUser();
                    attendanceMap.put(u.getId(), new AttendanceEntry(u.getId(), u.getFirstName(), u.getLastName(), "ON_SHIFT"));
                });

        leaveRequestRepository.findRequestersOnLeave(today)
                .forEach(u -> attendanceMap.put(u.getId(), new AttendanceEntry(u.getId(), u.getFirstName(), u.getLastName(), "ON_LEAVE")));

        List<AttendanceEntry> todayAttendance = new ArrayList<>(attendanceMap.values());

        CoverageProjection coverage = shiftRepository.getCoverageForManagerWeek(managerId, startOfWeek, endOfweek);

        CoverageEntry coverageEntry = new CoverageEntry(
                coverage.getAssignedEmployees(),
                coverage.getMinEmployees(),
                coverage.getUnderstaffedShiftsCount()
        );

        return new ManagerDashboardResponse(swapSummary, leaveSummary, pendingRequests, todayAttendance, coverageEntry);
    }

    @Override
    public EmployeeDashboardResponse getEmployeeDashboard(Long userId, LocalDate weekStart, LocalDate weekEnd) {
        List<ShiftAssignment> assignments = shiftAssignmentRepository
                .findByUserIdAndShiftDateBetween(userId, weekStart, weekEnd);

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<ShiftAssignment> activeAssignments = assignments.stream()
                .filter(sa -> sa.getStatus() == ShiftAssignmentStatus.ASSIGNED
                        || sa.getStatus() == ShiftAssignmentStatus.REQUEST_APPLIED)
                .toList();

        double workedHours = activeAssignments.stream()
                .map(ShiftAssignment::getShift)
                .filter(shift ->
                        shift.getShiftDate().isBefore(today)
                                || (shift.getShiftDate().isEqual(today)
                                && shift.getEndTime().isBefore(now))
                )
                .mapToDouble(this::calculateShiftHours)
                .sum();

        double remainingHours = activeAssignments.stream()
                .map(ShiftAssignment::getShift)
                .filter(shift ->
                        shift.getShiftDate().isAfter(today)
                                || (shift.getShiftDate().isEqual(today)
                                && !shift.getEndTime().isBefore(now))
                )
                .mapToDouble(this::calculateShiftHours)
                .sum();


        List<UpcomingShiftEntry> upcomingShifts = assignments.stream()
                .filter(sa -> sa.getStatus() == ShiftAssignmentStatus.ASSIGNED
                        || sa.getStatus() == ShiftAssignmentStatus.REQUEST_APPLIED)
                .filter(sa -> {
                    Shift s = sa.getShift();
                    return s.getShiftDate().isAfter(today)
                            || (s.getShiftDate().isEqual(today) && !s.getEndTime().isBefore(now));
                })
                .map(sa -> {
                    Shift shift = sa.getShift();
                    return new UpcomingShiftEntry(
                            shift.getId(), shift.getTitle(),
                            shift.getShiftDate(), shift.getStartTime(), shift.getEndTime()
                    );
                })
                .sorted(Comparator.comparing(UpcomingShiftEntry::shiftDate))
                .toList();

        List<UpcomingShiftEntry> completedShifts = assignments.stream()
                .filter(sa -> sa.getStatus() == ShiftAssignmentStatus.ASSIGNED
                        || sa.getStatus() == ShiftAssignmentStatus.REQUEST_APPLIED)
                .filter(sa -> {
                    Shift s = sa.getShift();
                    return s.getShiftDate().isBefore(today)
                            || (s.getShiftDate().isEqual(today) && s.getEndTime().isBefore(now));
                })
                .map(sa -> {
                    Shift shift = sa.getShift();
                    return new UpcomingShiftEntry(
                            shift.getId(), shift.getTitle(),
                            shift.getShiftDate(), shift.getStartTime(), shift.getEndTime()
                    );
                })
                .sorted(Comparator.comparing(UpcomingShiftEntry::shiftDate))
                .toList();

        List<ManagerApproval> userApprovals = managerApprovalRepository.findAllByUserInvolved(userId);

        TypeCounts requestSummary = computeCounts(userApprovals);

        List<PendingRequestEntry> pendingRequests = userApprovals.stream()
                .filter(a -> a.getStatus() == ApprovalStatus.PENDING_TARGET_APPROVAL
                        && a.getSwapRequest() != null
                        && a.getSwapRequest().getTargetUser().getId().equals(userId))
                .map(this::toPendingRequestEntry)
                .toList();

        HoursSummary hoursSummary = new HoursSummary(workedHours, remainingHours);

        return new EmployeeDashboardResponse(upcomingShifts, completedShifts, requestSummary, hoursSummary, pendingRequests);
    }

    private TypeCounts computeCounts(List<ManagerApproval> approvals) {
        long pending = approvals.stream().filter(a ->
                a.getStatus() == ApprovalStatus.PENDING_TARGET_APPROVAL
                        || a.getStatus() == ApprovalStatus.PENDING_MANAGER_APPROVAL).count();
        long approved = approvals.stream().filter(a ->
                a.getStatus() == ApprovalStatus.APPROVED).count();
        long rejected = approvals.stream().filter(a ->
                a.getStatus() == ApprovalStatus.MANAGER_REJECTED
                        || a.getStatus() == ApprovalStatus.TARGET_REJECTED).count();
        return new TypeCounts(pending, approved, rejected);
    }

    private PendingRequestEntry toPendingRequestEntry(ManagerApproval a) {
        String requesterFirstName = "", requesterLastName = "";
        String targetFirstName = null, targetLastName = null;
        Long requestId = null;

        if (a.getLeaveRequest() != null) {
            User requester = a.getLeaveRequest().getRequester();
            requesterFirstName = requester.getFirstName();
            requesterLastName = requester.getLastName();
            requestId = a.getLeaveRequest().getId();
        } else if (a.getSwapRequest() != null) {
            User requester = a.getSwapRequest().getRequester();
            User target = a.getSwapRequest().getTargetUser();
            requesterFirstName = requester.getFirstName();
            requesterLastName = requester.getLastName();
            targetFirstName = target.getFirstName();
            targetLastName = target.getLastName();
            requestId = a.getSwapRequest().getId();
        }

        return new PendingRequestEntry(
                a.getId(), requestId,
                a.getType().name(), a.getStatus().name(),
                requesterFirstName, requesterLastName,
                targetFirstName, targetLastName,
                a.getCreatedAt()
        );
    }

    private double calculateShiftHours(Shift shift) {
        long minutes = Duration.between(
                shift.getStartTime(),
                shift.getEndTime()
        ).toMinutes();

        return minutes / 60.0;
    }
}
