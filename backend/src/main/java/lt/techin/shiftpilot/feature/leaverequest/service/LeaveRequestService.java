package lt.techin.shiftpilot.feature.leaverequest.service;

import lt.techin.shiftpilot.feature.leaverequest.dto.CreateLeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.dto.LeaveRequestResponse;

public interface LeaveRequestService {
    LeaveRequestResponse createLeaveRequest(Long id, CreateLeaveRequest request);
}
