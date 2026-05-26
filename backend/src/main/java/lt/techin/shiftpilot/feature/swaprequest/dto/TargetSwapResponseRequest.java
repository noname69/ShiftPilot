package lt.techin.shiftpilot.feature.swaprequest.dto;

public record TargetSwapResponseRequest(
        Long swapRequestId,
        boolean accepted,
        String comment
) {}
