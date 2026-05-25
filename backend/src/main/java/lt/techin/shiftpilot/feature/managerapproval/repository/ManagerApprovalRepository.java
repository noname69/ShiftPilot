package lt.techin.shiftpilot.feature.managerapproval.repository;

import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ManagerApprovalRepository extends JpaRepository<ManagerApproval, Long> {

    List<ManagerApproval> findByManagerId(Long managerId);
}
