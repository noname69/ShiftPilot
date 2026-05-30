package lt.techin.shiftpilot.feature.leaverequest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.techin.shiftpilot.feature.managerapproval.model.ManagerApproval;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.user.model.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User requester;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_approval_id")
    private ManagerApproval approval;

    @OneToOne
    @JoinColumn(name = "assignment_id")
    private ShiftAssignment assignment;

    private String reason;

    private LocalDateTime outFrom;
    private LocalDateTime outTill;

    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

}
