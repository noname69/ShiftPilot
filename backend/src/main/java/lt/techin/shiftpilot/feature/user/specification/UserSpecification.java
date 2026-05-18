package lt.techin.shiftpilot.feature.user.specification;

import jakarta.persistence.criteria.Predicate;

import lt.techin.shiftpilot.feature.user.dto.UserFilter;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import lt.techin.shiftpilot.security.principal.UserPrincipal;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> withFilters(
            UserFilter filter,
            UserPrincipal currentUser
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // SEARCH
            if (filter.search() != null && !filter.search().isBlank()) {

                String search = "%" + filter.search().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("firstName")), search),
                                cb.like(cb.lower(root.get("lastName")), search),
                                cb.like(cb.lower(root.get("username")), search)
                        )
                );
            }

            // STATUS FILTER
            if (filter.status() != null) {
                predicates.add(
                        cb.equal(root.get("status"), filter.status())
                );
            }

            // ROLE FILTER
            if (filter.role() != null) {
                predicates.add(
                        cb.equal(root.get("role"), filter.role())
                );
            }

            // SECURITY FILTER
            if (currentUser.getRole() != UserRole.ADMIN) {

                predicates.add(
                        cb.notEqual(root.get("role"), UserRole.ADMIN)
                );

                predicates.add(
                        cb.equal(root.get("status"), UserStatus.ACTIVE)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
