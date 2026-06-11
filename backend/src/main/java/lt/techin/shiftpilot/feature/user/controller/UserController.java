package lt.techin.shiftpilot.feature.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.user.dto.*;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import lt.techin.shiftpilot.feature.user.service.UserService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/search")
    public UserListResponse getFilteredUsers(
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) String searchByFullName,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return userService.getFilteredUsers(status, role, searchByFullName, pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
            ) {
        return  userService.update(id, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/me/edit")
    public UserResponse editPersonalInformation(@Valid @RequestBody EditPersonalInformationRequest request,
                                                @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();

        return userService.editPersonalInformation(username, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}/restore")
    public void restore(@PathVariable Long id) {
        userService.restore(id);
    }

}