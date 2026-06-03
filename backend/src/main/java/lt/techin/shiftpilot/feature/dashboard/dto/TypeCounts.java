package lt.techin.shiftpilot.feature.dashboard.dto;

public record TypeCounts(
        long pendingCount,
        long approvedCount,
        long rejectedCount
) {
}
