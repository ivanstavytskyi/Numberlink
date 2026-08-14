package numberlink.dto.user.email.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ConfirmEmailChangeRequestDto(
        @NotBlank
        @Pattern(regexp = "^\\d{6}$", message = "Enter the 6-digit code.")
        String code
) {}
