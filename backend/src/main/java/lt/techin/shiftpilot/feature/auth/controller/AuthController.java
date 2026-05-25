package lt.techin.shiftpilot.feature.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.auth.dto.AuthResponse;
import lt.techin.shiftpilot.feature.auth.dto.LoginRequest;
import lt.techin.shiftpilot.feature.auth.dto.RefreshTokenRequest;
import lt.techin.shiftpilot.feature.auth.model.RefreshToken;
import lt.techin.shiftpilot.feature.auth.service.AuthService;
import lt.techin.shiftpilot.feature.auth.service.RefreshTokenService;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.security.jwt.JwtService;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import lt.techin.shiftpilot.security.principal.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        return authService.login(request.username(), request.password(), response);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshTokenRequest request, HttpServletResponse response) {
        return authService.refreshToken(request.refreshToken(), response);
    }

    // LOGOUT
//    @PostMapping("/logout")
//    public void logout(@RequestBody RefreshTokenRequest request, HttpServletResponse response) {
//        authService.logout(request.refreshToken(), response);
//    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        authService.logout(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();

        AuthResponse response = authService.getMe(username);

        return ResponseEntity.ok().body(response);
    }

}
