package lt.techin.shiftpilot.feature.user.mapper;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.repository.LeaveRequestRepository;
import lt.techin.shiftpilot.feature.shiftassignment.dto.AssigneeResponse;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.user.dto.CreateUserRequest;
import lt.techin.shiftpilot.feature.user.dto.UserResponse;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserMapper {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    public User toEntity(CreateUserRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .username(request.username())
                .password(request.password())
                .role(request.role())
                .build();
    }

    public UserResponse toResponse(User user) {

        List<LeaveRequest> leaveRequests = leaveRequestRepository
                .findByRequesterIdAndOutFromLessThanEqualAndOutTillGreaterThanEqual(user.getId(), LocalDate.now(), LocalDate.now());

        LocalDate outFrom = null;
        LocalDate outTill = null;

        if(!leaveRequests.isEmpty()) {
            outFrom = leaveRequests.getFirst().getOutFrom();
            outTill = leaveRequests.getFirst().getOutTill();
        }

        if(outFrom == null && outTill == null) {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                outFrom,
                outTill
        );
    }


    public AssigneeResponse toAssigneeResponse(User user, ShiftAssignmentStatus status, Long assigneeId) {
        return new AssigneeResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                status,
                assigneeId
        );
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .toList();
    }
}
