package lt.techin.shiftpilot.feature.managerapproval.repository;

import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ManagerApprovalRepository extends JpaRepository<ManagerApproval, Long> {

    List<ManagerApproval> findByManagerId(Long managerId);

    @Query("""
    SELECT ma
    FROM ManagerApproval ma
    LEFT JOIN ma.swapRequest sr
    LEFT JOIN ma.leaveRequest lr
    WHERE
        (sr.requester.id = :userId OR sr.targetUser.id = :userId)
        OR
        (lr.requester.id = :userId)
    """)
    List<ManagerApproval> findAllByUserInvolved(@Param("userId") Long userId);

    @Query("""
    SELECT ma
    FROM ManagerApproval ma
    WHERE ma.manager.id = :managerId
    AND ma.createdAt  BETWEEN :weekStart AND :weekEnd
    """)
    List<ManagerApproval> findByManagerIdAndDateBetween(Long managerId, LocalDateTime weekStart, LocalDateTime weekEnd);
}
