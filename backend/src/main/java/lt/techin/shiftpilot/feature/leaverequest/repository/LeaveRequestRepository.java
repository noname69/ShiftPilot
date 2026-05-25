package lt.techin.shiftpilot.feature.leaverequest.repository;

import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
}
