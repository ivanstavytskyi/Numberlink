package numberlink.dto.user.profile.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequestDto(
        @NotBlank @Size(min = 3, max = 32)
        @Pattern(regexp = "^[a-zA-Z0-9_]{3,32}$")
        String username,
        @Email
        String email
) {}
