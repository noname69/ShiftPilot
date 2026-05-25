package lt.techin.shiftpilot.feature.managerapproval.model;

public enum ApprovalStatus {

    PENDING_TARGET_APPROVAL,
    TARGET_REJECTED,
    PENDING_MANAGER_APPROVAL,
    MANAGER_REJECTED,
    APPROVED
//    CANCELLED nzn ar sito reikia, nes CANCEL isivaizduoju TARGET_REJECTED, arba MANAGER_REJECTED
}
