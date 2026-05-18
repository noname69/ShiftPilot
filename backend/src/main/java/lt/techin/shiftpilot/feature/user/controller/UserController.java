package lt.techin.shiftpilot.feature.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.user.dto.CreateUserRequest;
import lt.techin.shiftpilot.feature.user.dto.UpdateUserRequest;
import lt.techin.shiftpilot.feature.user.dto.UserFilter;
import lt.techin.shiftpilot.feature.user.dto.UserResponse;
import lt.techin.shiftpilot.feature.user.service.UserService;
import lt.techin.shiftpilot.response.ApiResponse;
import lt.techin.shiftpilot.response.ApiResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAll(
            UserFilter filter,
            HttpServletRequest request) {
        return ApiResponseFactory.success(
                userService.getAll(filter),
                "tekst",
                request);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PutMapping("/{id}")
    public UserResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
            ) {
        return  userService.update(id, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @PatchMapping("/{id}/restore")
    public void restore(@PathVariable Long id) {
        userService.restore(id);
    }

    // GET /api/users/me
    @GetMapping("/me")
    public UserResponse me() {
        return userService.getCurrentUser();
    }


    // GET /api/users/search?query=john
    // PATCH /api/users/{id}/role
    // PATCH /api/users/{id}/password

}
