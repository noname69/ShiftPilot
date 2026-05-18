package lt.techin.shiftpilot.feature.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.auth.dto.AuthResponse;
import lt.techin.shiftpilot.feature.auth.dto.LoginRequest;
import lt.techin.shiftpilot.feature.auth.dto.RefreshTokenRequest;
import lt.techin.shiftpilot.feature.auth.model.RefreshToken;
import lt.techin.shiftpilot.feature.auth.service.AuthService;
import lt.techin.shiftpilot.feature.auth.service.RefreshTokenService;
import lt.techin.shiftpilot.security.jwt.JwtService;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // LOGIN
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request.username(), request.password());
    }

    // REFRESH TOKEN
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request.refreshToken());
    }

    // LOGOUT
    @PostMapping("/logout")
    public void logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.refreshToken());
    }
}
