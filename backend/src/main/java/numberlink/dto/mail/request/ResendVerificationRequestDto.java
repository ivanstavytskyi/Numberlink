package numberlink.dto.mail.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResendVerificationRequestDto(
        @NotBlank
        @Email
        @Size(max = 255)
        String email
) {}
