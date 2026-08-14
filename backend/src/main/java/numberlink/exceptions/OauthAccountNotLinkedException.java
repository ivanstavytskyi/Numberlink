package numberlink.exceptions;

import numberlink.entity.enums.OauthProvider;
import org.springframework.http.HttpStatus;

public class OauthAccountNotLinkedException extends ApiException {
    public OauthAccountNotLinkedException(OauthProvider provider) {
        super(
                HttpStatus.NOT_FOUND,
                "OAUTH_NOT_LINKED",
                label(provider) + " is not connected to this account."
        );
    }

    private static String label(OauthProvider provider) {
        return provider == OauthProvider.GOOGLE ? "Google" : "GitHub";
    }
}
