package lt.techin.shiftpilot.feature.reschedulerequest.model;

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
public class RescheduleRequest {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    private String reason;
    private RequestStatus status;
    private Long reviewedByUserId;
    LocalDateTime createdAt;
    LocalDateTime reviewedAt;
}
