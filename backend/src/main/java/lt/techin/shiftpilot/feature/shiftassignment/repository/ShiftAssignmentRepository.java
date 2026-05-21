package lt.techin.shiftpilot.feature.shiftassignment.repository;

import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftAssignmentRepository extends JpaRepository<ShiftAssignment, Long> {
}
