package lt.techin.shiftpilot.feature.shift.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Shift {

    @Id
    Long id;
    String title;
    String description;
    LocalDate shiftDate;
    Time startTime;
    Time endTime;
    int minEmployees;
    ShiftStatus status;
    Long createdByUserId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


    public Shift(Long id, String title, String description, LocalDate shiftDate, Time startTime, Time endTime, int minEmployees, ShiftStatus status, Long createdByUserId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.shiftDate = shiftDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minEmployees = minEmployees;
        this.status = status;
        this.createdByUserId = createdByUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
