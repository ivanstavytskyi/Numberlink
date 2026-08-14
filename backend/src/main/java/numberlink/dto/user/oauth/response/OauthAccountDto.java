package numberlink.dto.user.oauth.response;

public record OauthAccountDto(
        String provider,
        String displayName,
        String email
) {
}
