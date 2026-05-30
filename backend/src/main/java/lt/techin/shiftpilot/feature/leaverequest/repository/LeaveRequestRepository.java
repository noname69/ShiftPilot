package lt.techin.shiftpilot.feature.leaverequest.repository;

import lt.techin.shiftpilot.feature.leaverequest.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    boolean existsByAssignmentId(Long assignmentId);

    @Query("""
        select lr from LeaveRequest lr
        where lr.requester.id = :userId
        and lr.approval.status = lt.techin.shiftpilot.feature.managerapproval.model.ApprovalStatus.APPROVED
        and lr.approval.type in (
            lt.techin.shiftpilot.feature.managerapproval.model.RequestType.ILL,
            lt.techin.shiftpilot.feature.managerapproval.model.RequestType.ABSENCE,
            lt.techin.shiftpilot.feature.managerapproval.model.RequestType.VACATION
        )
        and lr.outFrom <= :weekEnd
        and lr.outTill >= :weekStart
    """)
    List<LeaveRequest> findApprovedLeaveRequestsInRange(
            @Param("userId") Long userId,
            @Param("weekStart") LocalDateTime weekStart,
            @Param("weekEnd") LocalDateTime weekEnd
    );
}
