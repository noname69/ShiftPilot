package lt.techin.shiftpilot.feature.shiftassignment.repository;

import jakarta.persistence.criteria.Predicate;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class ShiftAssignmentSpecifications {

    private ShiftAssignmentSpecifications() {}

    public static Specification<ShiftAssignment> withFilters(
            Long userId,
            LocalDate weekStart,
            LocalDate weekEnd
    ) {
        Specification<ShiftAssignment> spec = (root, query, builder) ->
                builder.conjunction();

        spec = spec.and(hasShiftDateBetween(weekStart, weekEnd));

        if (userId != null) {
            spec = spec.and(hasUserId(userId));
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
}