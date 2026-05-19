package lt.techin.shiftpilot.feature.shift.model;

import jakarta.persistence.Entity;


public enum ShiftStatus {
    OPEN,
    ONGOING,
    COMPLETED,
    CANCELLED
}
