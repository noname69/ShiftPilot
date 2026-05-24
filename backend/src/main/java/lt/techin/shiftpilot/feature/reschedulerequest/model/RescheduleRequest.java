package lt.techin.shiftpilot.feature.reschedulerequest.model;

import jakarta.persistence.*;
import lombok.*;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.shiftassignment.model.ShiftAssignment;
import lt.techin.shiftpilot.feature.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "reschedule_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RescheduleRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who created request
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    // User with whom requester wants to swap
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;

    // Requester's assignment
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_assignment_id", nullable = false)
    private ShiftAssignment requesterAssignment;

    // Target user's assignment
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_assignment_id", nullable = false)
    private ShiftAssignment targetAssignment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RescheduleRequestStatus status;

    @Column(length = 500)
    private String reason;

    private LocalDateTime targetRespondedAt;
    private LocalDateTime managerRespondedAt;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = RescheduleRequestStatus.PENDING_TARGET_APPROVAL;
        }
    }
}
