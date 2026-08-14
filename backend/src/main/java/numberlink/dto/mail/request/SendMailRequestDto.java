package numberlink.dto.mail.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMailRequestDto(
        @NotBlank
        @Email
        @Size(max = 255)
        String to,

        @NotBlank
        @Size(max = 200)
        String subject,

        @NotBlank
        @Size(max = 10_000)
        String body
) {}
