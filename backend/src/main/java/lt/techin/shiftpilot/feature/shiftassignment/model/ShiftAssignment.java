package lt.techin.shiftpilot.feature.shiftassignment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ShiftAssignment {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long assignedByUserId;
    ShiftAssignmentStatus status;
    LocalDateTime assignedAt;
    LocalDateTime removedAt;

    public ShiftAssignment(Long id, Shift shift, User user, Long assignedByUserId, ShiftAssignmentStatus status, LocalDateTime assignedAt, LocalDateTime removedAt) {
        this.id = id;
        this.shift = shift;
        this.user = user;
        this.assignedByUserId = assignedByUserId;
        this.status = status;
        this.assignedAt = assignedAt;
        this.removedAt = removedAt;
    }
}
