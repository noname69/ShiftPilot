package lt.techin.shiftpilot.feature.shiftDraft.repository;

import lt.techin.shiftpilot.feature.shiftDraft.model.ShiftDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftDraftRepository extends JpaRepository<ShiftDraft, Long> {
}
