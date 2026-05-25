package lt.techin.shiftpilot.feature.leaverequest.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.assignment.AssignmentNotFoundException;
import lt.techin.shiftpilot.feature.leaverequest.dto.CreateLeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.dto.LeaveRequestResponse;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.repository.LeaveRequestRepository;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.managerapproval.repository.ManagerApprovalRepository;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeaveRequestServiceImpl implements LeaveRequestService{

    private final LeaveRequestRepository leaveRequestRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final ManagerApprovalRepository managerApprovalRepository;

    @Override
    @Transactional
    public LeaveRequestResponse createLeaveRequest(Long requesterId, Long assignmentId, CreateLeaveRequest request) {

        ShiftAssignment assignment = shiftAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException(assignmentId));

        User manager = assignment.getAssignedBy();

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setAssignment(assignment);
        leaveRequest.setReason(request.getReason());

        ManagerApproval approval = new ManagerApproval();
        approval.setManager(manager);
        approval.setType(request.getType());
        approval.setStatus(ApprovalStatus.PENDING_MANAGER_APPROVAL);

        leaveRequest.setApproval(approval);
        approval.setLeaveRequest(leaveRequest);

        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);

        return new LeaveRequestResponse(
                savedRequest.getId(),
                savedRequest.getApproval().getId(),
                requesterId,
                assignmentId,
                savedRequest.getStatus()
        );
    }
}
