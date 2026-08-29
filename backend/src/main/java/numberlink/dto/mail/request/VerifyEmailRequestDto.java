package numberlink.dto.mail.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequestDto(
        @NotBlank(message = "token is required")
        String token
) {
}
