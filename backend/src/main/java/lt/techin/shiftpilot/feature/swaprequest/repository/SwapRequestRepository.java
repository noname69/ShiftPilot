package lt.techin.shiftpilot.feature.swaprequest.repository;

import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequest;
import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RescheduleRequestRepository extends JpaRepository<SwapRequest, Long> {
    boolean existsByRequesterAssignmentIdAndTargetAssignmentIdAndStatusIn(
            Long requesterAssignmentId,
            Long targetAssignmentId,
            List<SwapRequestStatus> statuses
    );
    List<SwapRequest> findAllByOrderByCreatedAtDesc();
    List<SwapRequest> findByRequesterIdOrTargetUserIdOrderByCreatedAtDesc(Long requesterId, Long targetId);
}
