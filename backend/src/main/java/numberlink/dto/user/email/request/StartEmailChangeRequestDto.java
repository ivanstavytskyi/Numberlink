package numberlink.dto.user.email.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StartEmailChangeRequestDto(
        @NotBlank @Email String email,
        String password
) {}
