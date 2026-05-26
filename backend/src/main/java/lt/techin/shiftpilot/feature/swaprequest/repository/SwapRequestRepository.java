package lt.techin.shiftpilot.feature.swaprequest.repository;

import lt.techin.shiftpilot.feature.swaprequest.model.SwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {

    List<SwapRequest> findAllByOrderByCreatedAtDesc();
    List<SwapRequest> findByRequesterIdOrTargetUserIdOrderByCreatedAtDesc(Long requesterId, Long targetId);
}
