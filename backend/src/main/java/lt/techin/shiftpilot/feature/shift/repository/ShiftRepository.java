package lt.techin.shiftpilot.feature.shift.repository;

import lt.techin.shiftpilot.feature.shift.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

}
