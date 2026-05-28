package lt.techin.shiftpilot.feature.shiftassignment.repository;

import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, Long> {

    List<ShiftAssignment> findByUser(User user);

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

    boolean existsByUserIdAndShiftId(Long userId, Long shiftId);
}
