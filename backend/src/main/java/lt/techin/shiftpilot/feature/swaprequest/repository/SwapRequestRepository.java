package lt.techin.shiftpilot.feature.swaprequest.repository;

import lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus;
import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {

    List<SwapRequest> findAllByOrderByCreatedAtDesc();
    List<SwapRequest> findByRequesterIdOrTargetUserIdOrderByCreatedAtDesc(Long requesterId, Long targetId);
    boolean existsByRequesterAssignmentIdAndTargetAssignmentIdAndApproval_StatusIn(
            Long requesterAssignmentId,
            Long targetAssignmentId,
            List<ApprovalStatus> statuses
    );

    @Query("SELECT sr FROM SwapRequest sr WHERE sr.requesterAssignment.id IN :ids OR sr.targetAssignment.id IN :ids")
    List<SwapRequest> findAllByAssignmentIds(@Param("ids") List<Long> ids);
}
