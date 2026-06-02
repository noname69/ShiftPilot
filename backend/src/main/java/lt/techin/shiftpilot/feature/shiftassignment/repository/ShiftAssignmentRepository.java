package lt.techin.shiftpilot.feature.shiftassignment.repository;

import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, Long> {

    List<ShiftAssignment> findByUser(User user);

    @Query("""
    select sa
    from ShiftAssignment sa
    where sa.user = :user
    and sa.status in ('ASSIGNED', 'REQUEST_APPLIED')
    """)
    List<ShiftAssignment> findByUserOrStatus(
            @Param("user") User user
    );

    @Query("""
        select sa.user
        from ShiftAssignment sa
        where sa.shift.id = :shiftId
        and sa.status = lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus.ASSIGNED
    """)
    List<User> findUsersByShiftId(@Param("shiftId") Long shiftId);

    @Query("""
        select sa
        from ShiftAssignment sa
        where sa.shift.id = :shiftId
    """)
    List<ShiftAssignment> findAllByShiftId(@Param("shiftId") Long shiftId);

    @Query("""
    select sa.status
    from ShiftAssignment sa
    where sa.user.id = :userId
    and sa.shift.id = :shiftId
    """)
    ShiftAssignmentStatus findStatusByUserIdAndShiftId(
            @Param("userId") Long userId,
            @Param("shiftId") Long shiftId
    );

    List<ShiftAssignment> findByShiftId(Long shiftId);

    List<ShiftAssignment> findByShiftIdAndStatus(Long shiftId, ShiftAssignmentStatus status);
    @Query("""
    select sa
    from ShiftAssignment sa
    where sa.shift.id = :shiftId
    and sa.user.id = :userId
    and sa.status = lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus.ASSIGNED
    """)
    Optional<ShiftAssignment> findAssignedByShiftIdAndUserId(
            @Param("shiftId") Long shiftId,
            @Param("userId") Long userId
    );

    @Query("""
    select sa
    from ShiftAssignment sa
    where sa.shift.id = :shiftId
    and sa.user.id = :userId
    """)
    Optional<ShiftAssignment> findByShiftIdAndUserId(
            @Param("shiftId") Long shiftId,
            @Param("userId") Long userId
    );

    boolean existsByUserIdAndShiftIdAndStatus(
            Long userId,
            Long shiftId,
            ShiftAssignmentStatus status
    );

    Optional<ShiftAssignment> findByUserIdAndShiftId(Long userId, Long shiftId);

    @Query("""
    SELECT sa FROM ShiftAssignment sa
    JOIN sa.shift s
    WHERE s.shiftDate BETWEEN :fromDate AND :tillDate
    """)
    List<ShiftAssignment> findAllInTimeFrame(
            @Param("fromDate") LocalDate fromDate,
            @Param("tillDate") LocalDate tillDate
    );

    @Query("""
    select sa
    from ShiftAssignment sa
    where sa.user.id = :userId
    and sa.shift.shiftDate >= :weekStart
    and sa.shift.shiftDate <= :weekEnd
    """)
    List<ShiftAssignment> findByUserIdAndShiftDateBetween(
            @Param("userId") Long userId,
            @Param("weekStart") LocalDate weekStart,
            @Param("weekEnd") LocalDate weekEnd
    );

    @Query("""
    select sa.user.id
    from ShiftAssignment sa
    where sa.shift.id in :shiftIds
""")
    Set<Long> findUserIdsByShiftIds(@Param("shiftIds") List<Long> shiftIds);
}