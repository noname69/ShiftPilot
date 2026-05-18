package lt.techin.shiftpilot.feature.user.mapper;

import lt.techin.shiftpilot.feature.user.dto.CreateUserRequest;
import lt.techin.shiftpilot.feature.user.dto.UserResponse;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public User toEntity(CreateUserRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .username(request.username())
                .password(request.password())
                .role(request.role())
//                .status(request.status())
                .build();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .toList();
    }
}
