package lt.techin.shiftpilot.feature.reschedulerequest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.techin.shiftpilot.feature.shift.model.Shift;
import lt.techin.shiftpilot.feature.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "reschedule_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RescheduleRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    private String reason;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revieved_bu_user_id")
    private User reviewedBy;

    LocalDateTime createdAt;
    LocalDateTime reviewedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
