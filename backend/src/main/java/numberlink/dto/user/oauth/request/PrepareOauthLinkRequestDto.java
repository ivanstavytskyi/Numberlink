package numberlink.dto.user.oauth.request;

import jakarta.validation.constraints.NotBlank;

public record PrepareOauthLinkRequestDto(
        @NotBlank(message = "Provider is required")
        String provider,
        String returnTo
) {
}
