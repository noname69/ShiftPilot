package lt.techin.shiftpilot.feature.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class EditPersonalInformationRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;


    private String oldPassword;

    @Pattern(
            regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,64}$",
            message = "Password must be 8-64 characters long and contain at least one uppercase letter, one lowercase letter, and one digit"
    )
    private String newPassword;
}
