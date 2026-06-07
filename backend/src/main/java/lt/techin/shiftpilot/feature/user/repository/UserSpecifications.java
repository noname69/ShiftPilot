package lt.techin.shiftpilot.feature.user.repository;

import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecifications {

    private UserSpecifications() {}

    public static Specification<User> withFilters(
            UserStatus status,
            UserRole role,
            String searchByFullName
    ) {
        Specification<User> specification = unrestricted();

        if (status != null) {
            specification = specification.and(hasStatus(status));
        }

        if (role != null) {
            specification = specification.and(hasRole(role));
        }

        if (searchByFullName != null && !searchByFullName.isBlank()) {
            specification = specification.and(hasNameContaining(searchByFullName));
        }

        return specification;
    }

    private static Specification<User> unrestricted() {
        return (root, query, builder) -> builder.conjunction();
    }

    public static Specification<User> hasStatus(UserStatus status) {
        return (root, query, builder) ->
                builder.equal(root.get("status"), status);
    }

    public static Specification<User> hasRole(UserRole role) {
        return (root, query, builder) ->
                builder.equal(root.get("role"), role);
    }

    public static Specification<User> hasNameContaining(String searchByFullName) {
        return (root, query, builder) -> {
            String pattern = "%" + searchByFullName.toLowerCase() + "%";

            return builder.or(
                    builder.like(
                            builder.lower(root.get("firstName")),
                            pattern
                    ),
                    builder.like(
                            builder.lower(root.get("lastName")),
                            pattern
                    )
            );
        };
    }
}