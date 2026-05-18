package lt.techin.shiftpilot.feature.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.user.DuplicateEmailException;
import lt.techin.shiftpilot.exception.user.DuplicateUsernameException;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.user.dto.UserFilter;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import lt.techin.shiftpilot.feature.user.dto.CreateUserRequest;
import lt.techin.shiftpilot.feature.user.dto.UpdateUserRequest;
import lt.techin.shiftpilot.feature.user.dto.UserResponse;
import lt.techin.shiftpilot.feature.user.mapper.UserMapper;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import lt.techin.shiftpilot.feature.user.specification.UserSpecification;
import lt.techin.shiftpilot.security.principal.UserPrincipal;
import lt.techin.shiftpilot.security.util.SecurityUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final SecurityUtils securityUtils;

    @Override
    public List<UserResponse> getAll(UserFilter filter) {

        UserPrincipal currentUser = securityUtils.getCurrentUser();

//        List<User> users;
//
//        if (currentUser.getRole() == UserRole.ADMIN) {
//            users = userRepository.findAll();
//        } else if(currentUser.getRole() == UserRole.HR) {
//            users = userRepository.findAllByStatus(UserStatus.INACTIVE);
//        } else {
//            users = userRepository.findAllByStatusAndRoleNot(
//                    UserStatus.ACTIVE,
//                    UserRole.ADMIN
//            );
//        }
        List<User> users = userRepository.findAll(
                UserSpecification.withFilters(filter, currentUser)
        );

        return userMapper.toResponseList(users);
    }

    @Override
    public UserResponse getById(Long id) {

        UserPrincipal currentUser = securityUtils.getCurrentUser();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if(currentUser.getRole() == UserRole.USER &&
        user.getRole() == UserRole.ADMIN) {
            throw new AccessDeniedException("You do not have permission to view this user");
        }

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if(userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateUsernameException(request.username());
        }

        User user = userMapper.toEntity(request);

        user.setPassword(encoder.encode(request.password()));

        User saved = userRepository.save(user);

        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userRepository.existsByEmail(request.email())
                && !user.getEmail().equals(request.email())) {

            throw new DuplicateEmailException(request.email());
        }

        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        }

        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }

        if (request.status() != null) {
            user.setStatus(request.status());
        }

        if (request.role() != null) {
            user.setRole(request.role());
        }

//        User saved = userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setStatus(UserStatus.INACTIVE);
    }

    @Override
    @Transactional
    public void restore(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setStatus(UserStatus.ACTIVE);
    }

    @Override
    public UserResponse getCurrentUser() {
        UserPrincipal currentUser = securityUtils.getCurrentUser();

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException(currentUser.getId()));

        return userMapper.toResponse(user);
    }
}
