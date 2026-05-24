package lt.techin.shiftpilot.feature.reschedulerequest.service;

import lt.techin.shiftpilot.feature.reschedulerequest.dto.CreateRescheduleRequestDto;
import lt.techin.shiftpilot.feature.reschedulerequest.dto.RescheduleRequestResponseDto;

public interface RescheduleRequestService {
    RescheduleRequestResponseDto createRequest(
            CreateRescheduleRequestDto request,
            String requesterUsername
    );
}
