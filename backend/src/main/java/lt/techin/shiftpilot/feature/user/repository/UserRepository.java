package lt.techin.shiftpilot.feature.user.repository;

import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserRole;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findAllByStatus(UserStatus status);
    List<User> findAllByStatusAndRoleNot(UserStatus status, UserRole role);
    Optional<User> findByUsername(String username);

    List<User> findByOutTillIsNotNullAndOutTillBefore(LocalDateTime now);

}
