package numberlink.dto.user.login.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank
        @Size(max = 255)
        String login,
        @NotBlank
        @Size(max = 128)
        String password
) {}