package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class IncompleteOauthProfileException extends ApiException {
    public IncompleteOauthProfileException(String message) {
        super(HttpStatus.BAD_REQUEST, "INCOMPLETE_OAUTH_PROFILE", message);
    }
}
