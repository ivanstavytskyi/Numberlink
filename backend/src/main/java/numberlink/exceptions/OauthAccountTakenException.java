package numberlink.exceptions;

import numberlink.entity.enums.OauthProvider;
import org.springframework.http.HttpStatus;

public class OauthAccountTakenException extends ApiException {
    public OauthAccountTakenException(OauthProvider provider) {
        super(
                HttpStatus.CONFLICT,
                "OAUTH_ACCOUNT_TAKEN",
                label(provider) + " is already linked to another NumberLink account."
        );
    }

    private static String label(OauthProvider provider) {
        return provider == OauthProvider.GOOGLE ? "This Google account" : "This GitHub account";
    }
}
