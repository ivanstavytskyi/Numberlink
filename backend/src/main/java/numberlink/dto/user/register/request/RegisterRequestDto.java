package numberlink.dto.user.register.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank
        @Size(min = 3, max = 32)
        @Pattern(
                regexp = "^[a-zA-Z0-9_]{3,32}$",
                message = "Username may contain letters, numbers and underscore (3–32)"
        )
        String username,
        @Email @NotBlank String email,
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
                message = "Password too weak"
        )
        @Size(max = 128)
        String password
) {}