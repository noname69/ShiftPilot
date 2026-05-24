package lt.techin.shiftpilot.feature.reschedulerequest.mapper;

import lt.techin.shiftpilot.feature.reschedulerequest.dto.RescheduleRequestResponseDto;
import lt.techin.shiftpilot.feature.reschedulerequest.model.RescheduleRequest;

public class RescheduleRequestMapper {

    public static RescheduleRequestResponseDto toResponse(RescheduleRequest request) {
        return new RescheduleRequestResponseDto(
                request.getId(),
                request.getRequester().getId(),
                request.getTargetUser().getId(),
                request.getRequesterAssignment().getId(),
                request.getTargetAssignment().getId(),
                request.getStatus(),
                request.getReason(),
                request.getCreatedAt(),
                request.getTargetRespondedAt(),
                request.getManagerRespondedAt(),
                request.getCompletedAt()
        );
    }
}
