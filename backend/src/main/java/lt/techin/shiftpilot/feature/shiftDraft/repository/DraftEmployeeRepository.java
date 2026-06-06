package lt.techin.shiftpilot.feature.shiftDraft.repository;

import lt.techin.shiftpilot.feature.shiftDraft.model.DraftEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DraftEmployeeRepository extends JpaRepository<DraftEmployee, Long> {
}