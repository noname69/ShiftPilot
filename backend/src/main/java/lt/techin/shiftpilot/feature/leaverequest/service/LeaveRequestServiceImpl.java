package lt.techin.shiftpilot.feature.leaverequest.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.assignment.AssignmentNotFoundException;
import lt.techin.shiftpilot.exception.assignment.RequestException;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.leaverequest.dto.CreateLeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.dto.LeaveRequestResponse;
import lt.techin.shiftpilot.feature.leaverequest.mapper.LeaveRequestMapper;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.repository.LeaveRequestRepository;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.notification.model.NotificationType;
import lt.techin.shiftpilot.feature.notification.service.NotificationService;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.shiftassignment.repository.ShiftAssignmentRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeaveRequestServiceImpl implements LeaveRequestService{

    private final LeaveRequestRepository leaveRequestRepository;
    private final ShiftAssignmentRepository shiftAssignmentRepository;
    private final UserRepository userRepository;
    private final LeaveRequestMapper leaveRequestMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public LeaveRequestResponse createLeaveRequest(Long requesterId, CreateLeaveRequest request) {

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException(requesterId));

        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new UserNotFoundException(request.getManagerId()));

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setReason(request.getReason());
        leaveRequest.setRequester(requester);
        leaveRequest.setOutFrom(request.getOutFrom());
        leaveRequest.setOutTill(request.getOutTill());

        ManagerApproval approval = new ManagerApproval();
        approval.setManager(manager);
        approval.setType(request.getType());
        approval.setStatus(ApprovalStatus.PENDING_MANAGER_APPROVAL);

        leaveRequest.setApproval(approval);
        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);

        sendLeaveRequestNotification(leaveRequest);

        return leaveRequestMapper.leaveRequestToResponse(savedRequest);
    }

    private void sendLeaveRequestNotification(LeaveRequest leaveRequest) {

        User requester = leaveRequest.getRequester();
        String message = String.format(
                "%s %s has applied leave request for %s.\nOut from %s until %s.",
                requester.getFirstName(), requester.getLastName(),
                leaveRequest.getApproval().getType(),
                leaveRequest.getOutFrom(),
                leaveRequest.getOutTill()
        );


        notificationService.createNotification(
                leaveRequest.getApproval().getManager(),
                "Leave request awaiting your approval",
                message,
                NotificationType.REQUEST_SUBMITTED,
                null);
    }
}

