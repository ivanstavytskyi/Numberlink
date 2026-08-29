package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class UnsupportedOauthProviderException extends ApiException {
    public UnsupportedOauthProviderException(String registrationId) {
        super(
                HttpStatus.BAD_REQUEST,
                "UNSUPPORTED_OAUTH_PROVIDER",
                "Unsupported OAuth provider: " + registrationId
        );
    }
}
