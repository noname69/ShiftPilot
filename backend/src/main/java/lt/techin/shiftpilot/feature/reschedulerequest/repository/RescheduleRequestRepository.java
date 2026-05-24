package lt.techin.shiftpilot.feature.reschedulerequest.repository;

import lt.techin.shiftpilot.feature.reschedulerequest.model.RescheduleRequest;
import lt.techin.shiftpilot.feature.reschedulerequest.model.RescheduleRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RescheduleRequestRepository extends JpaRepository<RescheduleRequest, Long> {
    boolean existsByRequesterAssignmentIdAndTargetAssignmentIdAndStatusIn(
            Long requesterAssignmentId,
            Long targetAssignmentId,
            List<RescheduleRequestStatus> statuses
    );
    List<RescheduleRequest> findAllByOrderByCreatedAtDesc();
    List<RescheduleRequest> findByRequesterIdOrTargetUserIdOrderByCreatedAtDesc(Long requesterId, Long targetId);
}
