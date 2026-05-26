package lt.techin.shiftpilot.feature.swaprequest.dto;

public record ManagerSwapResponseRequest(
        Long swapRequestId,
        boolean approved,
        String comment
) {
}
