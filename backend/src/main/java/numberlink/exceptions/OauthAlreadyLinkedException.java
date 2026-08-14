package numberlink.exceptions;

import numberlink.entity.enums.OauthProvider;
import org.springframework.http.HttpStatus;

public class OauthAlreadyLinkedException extends ApiException {
    public OauthAlreadyLinkedException(OauthProvider provider) {
        super(
                HttpStatus.CONFLICT,
                "OAUTH_ALREADY_LINKED",
                label(provider) + " is already connected to this account."
        );
    }

    private static String label(OauthProvider provider) {
        return provider == OauthProvider.GOOGLE ? "Google" : "GitHub";
    }
}
