package lt.techin.shiftpilot.feature.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import lt.techin.shiftpilot.feature.leaverequest.repository.LeaveRequestRepository;
import lt.techin.shiftpilot.feature.user.model.User;
import lt.techin.shiftpilot.feature.user.model.UserStatus;
import lt.techin.shiftpilot.feature.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserScheduler {

    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void activateUsersWhenAfterOutTillIsPassed() {

        System.out.println("Scheduler");

        LocalDate today = LocalDate.now();

        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByOutFromLessThanEqualAndOutTillGreaterThanEqual(today, today);
        List<User> users = userRepository.findUsersByStatusNotIn(List.of(UserStatus.ACTIVE, UserStatus.INACTIVE));

        leaveRequests.forEach(request -> {

            User user = request.getRequester();

            if(!user.getStatus().equals(UserStatus.valueOf(request.getApproval().getType().toString()))){
                user.setStatus(UserStatus.valueOf(request.getApproval().getType().toString()));
                userRepository.save(user);
            }
        });

        Set<Long> usersOnLeave = leaveRequests.stream()
                .map(r -> r.getRequester().getId())
                .collect(Collectors.toSet());

        for (User u : users) {
            if (usersOnLeave.contains(u.getId())) {
                continue;
            }

            if (u.getStatus() != UserStatus.ACTIVE) {
                u.setStatus(UserStatus.ACTIVE);
                userRepository.save(u);
            }
        }
    }
}