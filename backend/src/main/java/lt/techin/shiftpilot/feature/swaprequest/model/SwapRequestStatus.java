package lt.techin.shiftpilot.feature.reschedulerequest.model;

public enum RescheduleRequestStatus {
    PENDING_TARGET_APPROVAL,
    TARGET_REJECTED,

    PENDING_MANAGER_APPROVAL,

    MANAGER_REJECTED,

    COMPLETED,
    CANCELLED
}
