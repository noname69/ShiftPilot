package lt.techin.shiftpilot.feature.shiftassignment.repository;

import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class ShiftAssignmentSpecifications {

    private ShiftAssignmentSpecifications() {}

    public static Specification<ShiftAssignment> withFilters(
            Long userId,
            LocalDate weekStart,
            LocalDate weekEnd,
            LocalDate shiftDate,
            ShiftStatus shiftStatus
    ) {
        Specification<ShiftAssignment> spec =
                (root, query, builder) -> builder.conjunction();

        if (userId != null) {
            spec = spec.and(hasUserId(userId));
        }

        if (shiftStatus != null) {
            spec = spec.and(hasShiftStatus(shiftStatus));
        }

        if (shiftDate != null) {
            spec = spec.and(hasShiftDate(shiftDate));
        } else if (weekStart != null && weekEnd != null) {
            spec = spec.and(hasShiftDateBetween(weekStart, weekEnd));
        }

        return spec;
    }

    public static Specification<ShiftAssignment> hasShiftDateBetween(
            LocalDate weekStart,
            LocalDate weekEnd
    ) {
        return (root, query, builder) ->
                builder.between(
                        root.get("shift").get("shiftDate"),
                        weekStart,
                        weekEnd
                );
    }

    public static Specification<ShiftAssignment> hasUserId(Long userId) {
        return (root, query, builder) ->
                builder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<ShiftAssignment> hasShiftStatus(
            ShiftStatus shiftStatus
    ) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("shift").get("status"),
                        shiftStatus
                );
    }

    public static Specification<ShiftAssignment> hasShiftDate(
            LocalDate shiftDate
    ) {
        return (root, query, builder) ->
                builder.equal(
                        root.get("shift").get("shiftDate"),
                        shiftDate
                );
    }



}