package lt.techin.shiftpilot.feature.dashboard.service;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.dashboard.dto.*;
import lt.techin.shiftpilot.feature.leaverequest.repository.LeaveRequestRepository;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.managerapproval.model.RequestType;
import lt.techin.shiftpilot.feature.managerapproval.repository.ManagerApprovalRepository;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ManagerApprovalRepository managerApprovalRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @Override
    public ManagerDashboardResponse getManagerDashboard(Long managerId) {
        List<ManagerApproval> approvals = managerApprovalRepository.findByManagerId(managerId);

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
                .map(this::toPendingRequestEntry)
                .toList();

        LocalDate today = LocalDate.now();

        Set<User> employees = approvals.stream()
                .flatMap(a -> {
                    List<User> users = new ArrayList<>();
                    if (a.getLeaveRequest() != null) users.add(a.getLeaveRequest().getRequester());
                    if (a.getSwapRequest() != null) {
                        users.add(a.getSwapRequest().getRequester());
                        users.add(a.getSwapRequest().getTargetUser());
                    }
                    return users.stream();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User::getId))));

        Set<Long> onLeaveIds = leaveRequestRepository.findRequestersOnLeave(today)
                .stream().map(User::getId).collect(Collectors.toSet());

        Set<Long> onShiftIds = shiftAssignmentRepository.findAllInTimeFrame(today, today)
                .stream()
                .filter(sa -> sa.getStatus() == ShiftAssignmentStatus.ASSIGNED)
                .map(sa -> sa.getUser().getId())
                .collect(Collectors.toSet());

        List<AttendanceEntry> todayAttendance = employees.stream()
                .map(e -> {
                    String status;
                    if (onLeaveIds.contains(e.getId())) status = "ON_LEAVE";
                    else if (onShiftIds.contains(e.getId())) status = "ON_SHIFT";
                    else status = "UNSCHEDULED";
                    return new AttendanceEntry(e.getId(), e.getFirstName(), e.getLastName(), status);
                })
                .toList();

        return new ManagerDashboardResponse(swapSummary, leaveSummary, pendingRequests, todayAttendance);
    }

    @Override
    public EmployeeDashboardResponse getEmployeeDashboard(Long userId, LocalDate weekStart, LocalDate weekEnd) {
        List<ShiftAssignment> assignments = shiftAssignmentRepository
                .findByUserIdAndShiftDateBetween(userId, weekStart, weekEnd);

        List<UpcomingShiftEntry> upcomingShifts = assignments.stream()
                .filter(sa -> sa.getStatus() == ShiftAssignmentStatus.ASSIGNED
                        || sa.getStatus() == ShiftAssignmentStatus.REQUEST_APPLIED)
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

        return new EmployeeDashboardResponse(upcomingShifts, requestSummary);
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
}
