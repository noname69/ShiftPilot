package lt.techin.shiftpilot.feature.auth.service;

import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.auth.dto.AuthResponse;
import lt.techin.shiftpilot.feature.auth.model.RefreshToken;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import lt.techin.shiftpilot.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse login(String username, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                user.getRole()
        );
    }

    public AuthResponse refreshToken(String refreshTokenValue) {

        RefreshToken refreshToken = refreshTokenService.getByToken(refreshTokenValue);
        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(
                newAccessToken,
                refreshTokenValue,
                "Bearer",
                user.getRole()
        );
    }

    public void logout(String refreshTokenValue) {
        refreshTokenService.deleteByToken(refreshTokenValue);
    }
}
