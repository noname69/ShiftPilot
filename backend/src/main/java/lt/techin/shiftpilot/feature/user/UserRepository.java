package lt.techin.shiftpilot.feature.user;

import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    List<User> findAllByStatus(UserStatus status);
}
