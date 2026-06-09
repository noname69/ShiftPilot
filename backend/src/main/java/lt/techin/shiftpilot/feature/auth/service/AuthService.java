package lt.techin.shiftpilot.feature.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.auth.AuthenticationException;
import lt.techin.shiftpilot.exception.user.UserNotFoundException;
import lt.techin.shiftpilot.feature.auth.dto.AuthResponse;
import lt.techin.shiftpilot.feature.auth.model.RefreshToken;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import lt.techin.shiftpilot.security.jwt.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse login(String username,
                              String password,
                              HttpServletResponse response) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        ResponseCookie accessCookie = ResponseCookie.from(
                        "accessToken",
                        accessToken
                )
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(60))
                .sameSite("Lax")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(
                        "refreshToken",
                        refreshToken.getToken()
                )
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new AuthResponse(
                user.getFirstName(),
                user.getLastName(),
                "Login successful",
                user.getUsername(),
                user.getEmail(),
                user.getId(),
                user.getRole()
        );
    }

    public AuthResponse refreshToken(String refreshTokenValue,
                                     HttpServletResponse response) {

        RefreshToken refreshToken =
                refreshTokenService.getByToken(refreshTokenValue);

        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(user);

        ResponseCookie accessCookie = ResponseCookie.from(
                        "accessToken",
                        newAccessToken
                )
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return new AuthResponse(
                user.getFirstName(),
                user.getLastName(),
                "Token refreshed",
                user.getUsername(),
                user.getEmail(),
                user.getId(),
                user.getRole()
        );
    }

//    public void logout(String refreshTokenValue,
//                       HttpServletResponse response) {
//
//        refreshTokenService.deleteByToken(refreshTokenValue);
//
//        ResponseCookie accessCookie = ResponseCookie.from(
//                        "accessToken",
//                        ""
//                )
//                .httpOnly(true)
//                .secure(false)
//                .path("/")
//                .maxAge(0)
//                .sameSite("Lax")
//                .build();
//
//        ResponseCookie refreshCookie = ResponseCookie.from(
//                        "refreshToken",
//                        ""
//                )
//                .httpOnly(true)
//                .secure(false)
//                .path("/api/auth")
//                .maxAge(0)
//                .sameSite("Lax")
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
//        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
//    }

    public void logout(HttpServletResponse response) {

        ResponseCookie accessCookie = ResponseCookie.from(
                        "accessToken",
                        ""
                )
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    }

    public AuthResponse getMe(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("User with username '" + username + "' is not authenticated."));

        return new AuthResponse(user.getFirstName(), user.getLastName(), "Authenticated", user.getUsername(), user.getEmail(), user.getId(), user.getRole());

    }
}
