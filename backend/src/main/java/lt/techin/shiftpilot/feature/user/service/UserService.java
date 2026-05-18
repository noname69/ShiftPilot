package lt.techin.shiftpilot.feature.user.service;

import lt.techin.shiftpilot.feature.user.dto.CreateUserRequest;
import lt.techin.shiftpilot.feature.user.dto.UpdateUserRequest;
import lt.techin.shiftpilot.feature.user.dto.UserFilter;
import lt.techin.shiftpilot.feature.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll(UserFilter filter);
//    List<UserResponse> getAll();
//    List<UserResponse> getAllActive();
//    List<UserResponse> getAllInactive();
    UserResponse getById(Long id);
    UserResponse createUser(CreateUserRequest request);
    UserResponse update(Long id, UpdateUserRequest request);
    void delete(Long id);
    void restore(Long id);
    UserResponse getCurrentUser();
}
