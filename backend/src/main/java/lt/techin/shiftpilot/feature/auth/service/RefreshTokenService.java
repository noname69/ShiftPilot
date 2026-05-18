package lt.techin.shiftpilot.feature.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.exception.auth.ExpiredRefreshTokenException;
import lt.techin.shiftpilot.exception.auth.InvalidRefreshTokenException;
import lt.techin.shiftpilot.feature.auth.model.RefreshToken;
import lt.techin.shiftpilot.feature.auth.repository.RefreshTokenRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(User user) {
        System.out.println("refresh");
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();

        long refreshTokenDuration = 1000L * 60 * 60 * 24 * 7;
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDuration))
                .build();

        return refreshTokenRepository.save(token);
    }

    public void verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new ExpiredRefreshTokenException("Refresh token expired");
        }

    }

    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
