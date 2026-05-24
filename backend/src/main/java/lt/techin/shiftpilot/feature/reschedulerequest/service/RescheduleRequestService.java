package lt.techin.shiftpilot.feature.reschedulerequest.service;

import jakarta.transaction.Transactional;
import lt.techin.shiftpilot.feature.reschedulerequest.dto.CreateRescheduleRequestDto;
import lt.techin.shiftpilot.feature.reschedulerequest.dto.RescheduleRequestResponseDto;

import java.util.List;

public interface RescheduleRequestService {
    RescheduleRequestResponseDto createRequest(
            CreateRescheduleRequestDto request,
            String requesterUsername
    );

    List<RescheduleRequestResponseDto> getAllRequests();
    List<RescheduleRequestResponseDto> getMyRequests(String username);
}
