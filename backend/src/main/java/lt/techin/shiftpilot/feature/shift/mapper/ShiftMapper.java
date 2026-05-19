package lt.techin.shiftpilot.feature.shift.mapper;

import lt.techin.shiftpilot.feature.shift.dto.ShiftCreateRequest;
import lt.techin.shiftpilot.feature.shift.dto.ShiftResponse;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import org.springframework.stereotype.Component;

@Component
public class ShiftMapper {

    public Shift toEntity(ShiftCreateRequest request) {
        return Shift.builder()
                .title(request.title())
                .description(request.description())
                .shiftDate(request.shiftDate())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .minEmployees(request.minEmployees())
                .status(ShiftStatus.OPEN)
                .build();
    }

    public ShiftResponse toResponse(Shift shift) {
        return new ShiftResponse(
                shift.getId(),
                shift.getTitle(),
                shift.getDescription(),
                shift.getShiftDate(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getMinEmployees(),
                shift.getStatus(),
                shift.getCreatedBy() != null ? shift.getCreatedBy().getId() : null,
                shift.getCreatedBy() != null ? shift.getCreatedBy().getUsername() : null
        );
    }
}