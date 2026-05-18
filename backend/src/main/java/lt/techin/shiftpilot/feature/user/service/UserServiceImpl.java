package lt.techin.shiftpilot.feature.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.user.DuplicateEmailException;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.user.UserRepository;
import lt.techin.shiftpilot.feature.user.dto.CreateUserRequest;
import lt.techin.shiftpilot.feature.user.dto.UpdateUserRequest;
import lt.techin.shiftpilot.feature.user.dto.UserResponse;
import lt.techin.shiftpilot.feature.user.mapper.UserMapper;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    @Override
    public List<UserResponse> getAll(boolean includeInactive) {
        List<User> users = includeInactive
                ? userRepository.findAll()
                : userRepository.findAllByStatus(UserStatus.ACTIVE);

        return userMapper.toResponseList(users);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if(userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
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
}
