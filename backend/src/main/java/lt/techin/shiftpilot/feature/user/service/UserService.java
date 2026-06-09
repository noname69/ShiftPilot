package lt.techin.shiftpilot.feature.user.service;

import jakarta.validation.Valid;
import lt.techin.shiftpilot.feature.shift.model.ShiftStatus;
import lt.techin.shiftpilot.feature.user.dto.*;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll();
    UserResponse getById(Long id);
    UserResponse createUser(CreateUserRequest request);
    UserResponse update(Long id, UpdateUserRequest request);
    void delete(Long id);
    void restore(Long id);
    UserListResponse getFilteredUsers(UserStatus status, UserRole userRole, String searchByFullName, Pageable pageable);
    UserResponse editPersonalInformation(String username, EditPersonalInformationRequest request);
}
