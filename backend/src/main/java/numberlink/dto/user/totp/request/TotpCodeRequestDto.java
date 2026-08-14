package numberlink.dto.user.totp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TotpCodeRequestDto(
        @NotBlank(message = "Enter the 6-digit code")
        @Pattern(regexp = "\\d{6}", message = "Enter the 6-digit code")
        String code
) {
}
