package lt.techin.shiftpilot.feature.shiftDraft.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.techin.shiftpilot.feature.user.model.User;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DraftEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User draftEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_draft_id")
    private ShiftDraft shiftDraft;
}
