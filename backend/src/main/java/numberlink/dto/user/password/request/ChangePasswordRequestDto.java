package numberlink.dto.user.password.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(
        @NotBlank
        @Size(max = 128)
        String currentPassword,
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
                message = "Password needs 8+ characters with upper, lower, a number, and a symbol."
        )
        @Size(max = 128)
        String newPassword
) {}
