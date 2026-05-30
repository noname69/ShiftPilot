package lt.techin.shiftpilot.feature.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserScheduler {

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void activateUsersWhenAfterOutTillIsPassed() {

        List<User> users = userRepository.findByOutTillIsNotNullAndOutTillBefore(LocalDateTime.now());

        if(!users.isEmpty()) {
            users.forEach(user -> {
                user.setOutFrom(null);
                user.setOutTill(null);
                user.setStatus(UserStatus.ACTIVE);
                userRepository.save(user);
            });
        }
    }
}