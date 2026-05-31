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

@Service
@RequiredArgsConstructor
public class UserScheduler {

    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void activateUsersWhenAfterOutTillIsPassed() {

        LocalDate today = LocalDate.now();

        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByOutTillGreaterThanEqual(today);

        leaveRequests.forEach(request -> {

            User user = request.getRequester();

            if (!today.isBefore(request.getOutFrom())
                    && !today.isAfter(request.getOutTill())) {

                user.setStatus(UserStatus.valueOf(request.getApproval().getType().toString()));
                userRepository.save(user);
            }
        });
    }
}