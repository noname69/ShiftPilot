package lt.techin.shiftpilot.feature.auth.repository;

import lt.techin.shiftpilot.feature.auth.model.RefreshToken;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
    void deleteByToken(String token);
}
