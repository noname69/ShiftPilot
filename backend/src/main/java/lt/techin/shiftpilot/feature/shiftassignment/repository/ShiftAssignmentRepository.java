package lt.techin.shiftpilot.feature.shiftassignment.repository;

import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignmentStatus;
import lt.techin.shiftpilot.feature.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, Long> {

    List<ShiftAssignment> findByUser(User user);

    @Query("""
        select sa.user
        from ShiftAssignment sa
        where sa.shift.id = :shiftId
    """)
    List<User> findUsersByShiftId(@Param("shiftId") Long shiftId);

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
}
