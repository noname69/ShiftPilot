package lt.techin.shiftpilot.feature.user.dto;

public record UserSummary(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
