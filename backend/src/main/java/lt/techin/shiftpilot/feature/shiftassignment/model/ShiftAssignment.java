package lt.techin.shiftpilot.feature.shiftassignment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "shift_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "assigned_by_user_id")
    private User assignedBy;

    @Enumerated(EnumType.STRING)
    private ShiftAssignmentStatus status;

    private LocalDateTime assignedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime removedAt;

    @PrePersist
    public void onAssign() {
        this.assignedAt = LocalDateTime.now();
        this.status = ShiftAssignmentStatus.ASSIGNED;
    }



}
