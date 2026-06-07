package lt.techin.shiftpilot.feature.managerapproval.repository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequest;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.JoinType;

import java.time.LocalDateTime;

public final class ManagerApprovalSpecifications {

    private ManagerApprovalSpecifications() {}

    public static Specification<ManagerApproval> withFilters(
            Long managerId,
            Long requesterId,
            ApprovalStatus status,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            String requester
    ) {
        Specification<ManagerApproval> spec = unrestricted();

        if (managerId != null) {
            spec = spec.and(hasManager(managerId));
        }

        if (requesterId != null) {
            spec = spec.and(hasRequesterId(requesterId));
        }

        if (status != null) {
            spec = spec.and(hasStatus(status));
        }

        if (createdFrom != null) {
            spec = spec.and(hasCreatedFrom(createdFrom));
        }

        if (createdTo != null) {
            spec = spec.and(hasCreatedTo(createdTo));
        }
        if (requester != null && !requester.isBlank()) {
            spec = spec.and(hasRequester(requester));
        }

        return spec;
    }

    public static Specification<ManagerApproval> hasManager(Long managerId) {
        return (root, query, builder) ->
                builder.equal(root.get("manager").get("id"), managerId);
    }

    public static Specification<ManagerApproval> hasRequesterId(Long requesterId) {
        return (root, query, builder) -> {

            Join<ManagerApproval, LeaveRequest> leaveRequest =
                    root.join("leaveRequest", JoinType.LEFT);

            Join<ManagerApproval, SwapRequest> swapRequest =
                    root.join("swapRequest", JoinType.LEFT);

            Predicate leaveMatch = builder.equal(
                    leaveRequest.get("requester").get("id"),
                    requesterId
            );

            Predicate swapMatch = builder.equal(
                    swapRequest.get("requester").get("id"),
                    requesterId
            );

            return builder.or(leaveMatch, swapMatch);
        };
    }

    private static Specification<ManagerApproval> unrestricted() {
        return (root, query, builder) -> builder.conjunction();
    }

    public static Specification<ManagerApproval> hasStatus(ApprovalStatus status) {
        return (root, query, builder) ->
                builder.equal(root.get("status"), status);
    }

    public static Specification<ManagerApproval> hasCreatedFrom(LocalDateTime from) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<ManagerApproval> hasCreatedTo(LocalDateTime to) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("createdAt"), to);
    }

    public static Specification<ManagerApproval> hasRequester(String search) {
        return (root, query, builder) -> {

            String pattern = "%" + search.toLowerCase() + "%";

            Join<ManagerApproval, LeaveRequest> leaveRequest =
                    root.join("leaveRequest", JoinType.LEFT);

            Join<LeaveRequest, User> leaveRequester =
                    leaveRequest.join("requester", JoinType.LEFT);

            Join<ManagerApproval, SwapRequest> swapRequest =
                    root.join("swapRequest", JoinType.LEFT);

            Join<SwapRequest, User> swapRequester =
                    swapRequest.join("requester", JoinType.LEFT);

            return builder.or(
                    builder.like(builder.lower(leaveRequester.get("firstName")), pattern),
                    builder.like(builder.lower(leaveRequester.get("lastName")), pattern),
                    builder.like(builder.lower(leaveRequester.get("email")), pattern),

                    builder.like(builder.lower(swapRequester.get("firstName")), pattern),
                    builder.like(builder.lower(swapRequester.get("lastName")), pattern),
                    builder.like(builder.lower(swapRequester.get("email")), pattern)
            );
        };
    }

//    public static Specification<ManagerApproval> withFilters(
//            Long userId,
//            ApprovalStatus status,
//            LocalDateTime createdFrom,
//            LocalDateTime createdTo,
//            String requester
//    ) {
//        Specification<ManagerApproval> spec = unrestricted();
//
//        if (userId != null) {
//            spec = spec.and(hasUser(userId));
//        }
//
//        if (status != null) {
//            spec = spec.and(hasStatus(status));
//        }
//
//        if (createdFrom != null) {
//            spec = spec.and(hasCreatedFrom(createdFrom));
//        }
//
//        if (createdTo != null) {
//            spec = spec.and(hasCreatedTo(createdTo));
//        }
//
//        if (requester != null && !requester.isBlank()) {
//            spec = spec.and(hasRequester(requester));
//        }
//
//        return spec;
//    }
//
//    private static Specification<ManagerApproval> unrestricted() {
//        return (root, query, builder) -> builder.conjunction();
//    }
//
//    public static Specification<ManagerApproval> hasUser(Long userId) {
//        return (root, query, builder) ->
//                builder.equal(root.get("manager").get("id"), userId);
//    }
//
//    public static Specification<ManagerApproval> hasStatus(ApprovalStatus status) {
//        return (root, query, builder) ->
//                builder.equal(root.get("status"), status);
//    }
//
//    public static Specification<ManagerApproval> hasCreatedFrom(LocalDateTime from) {
//        return (root, query, builder) ->
//                builder.greaterThanOrEqualTo(root.get("createdAt"), from);
//    }
//
//    public static Specification<ManagerApproval> hasCreatedTo(LocalDateTime to) {
//        return (root, query, builder) ->
//                builder.lessThanOrEqualTo(root.get("createdAt"), to);
//    }
//
//    public static Specification<ManagerApproval> hasRequester(String search) {
//        return (root, query, builder) -> {
//
//            String pattern = "%" + search.toLowerCase() + "%";
//
//            Join<ManagerApproval, LeaveRequest> leaveRequest =
//                    root.join("leaveRequest", JoinType.LEFT);
//
//            Join<LeaveRequest, User> leaveRequester =
//                    leaveRequest.join("requester", JoinType.LEFT);
//
//            Join<ManagerApproval, SwapRequest> swapRequest =
//                    root.join("swapRequest", JoinType.LEFT);
//
//            Join<SwapRequest, User> swapRequester =
//                    swapRequest.join("requester", JoinType.LEFT);
//
//            return builder.or(
//                    builder.like(builder.lower(leaveRequester.get("firstName")), pattern),
//                    builder.like(builder.lower(leaveRequester.get("lastName")), pattern),
//                    builder.like(builder.lower(leaveRequester.get("email")), pattern),
//
//                    builder.like(builder.lower(swapRequester.get("firstName")), pattern),
//                    builder.like(builder.lower(swapRequester.get("lastName")), pattern),
//                    builder.like(builder.lower(swapRequester.get("email")), pattern)
//            );
//        };
//    }
}