package lt.techin.shiftpilot.feature.shift.repository;

import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class ShiftSpecifications {

    private ShiftSpecifications() {}

    public static Specification<Shift> withFilters(
            ShiftStatus status,
            LocalDate dateFrom,
            LocalDate dateTo,
            String createdBy
    ) {
        Specification<Shift> specification = notDeleted();

        if (status != null) {
            specification = specification.and(hasStatus(status));
        }

        if (dateFrom != null) {
            specification = specification.and(hasDateFrom(dateFrom));
        }

        if (dateTo != null) {
            specification = specification.and(hasDateTo(dateTo));
        }

        if (createdBy != null) {
            specification = specification.and(hasCreatedBy(createdBy));
        }

        return specification;
    }

    private static Specification<Shift> unrestricted() {
        return (root, query, builder) -> builder.conjunction();
    }

    private static Specification<Shift> notDeleted() {
        return (root, query, builder) ->
                builder.notEqual(root.get("status"), ShiftStatus.DELETED);
    }

    public static Specification<Shift> hasStatus(ShiftStatus status) {
        return (root, query, builder) ->
                builder.equal(root.get("status"), status);
    }

    public static Specification<Shift> hasDateFrom(LocalDate dateFrom) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("shiftDate"), dateFrom);
    }

    public static Specification<Shift> hasDateTo(LocalDate dateTo) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("shiftDate"), dateTo);
    }

    public static Specification<Shift> hasCreatedBy(String username) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("createdBy").get("username")), "%" + username.toLowerCase() + "%");
    }

}
