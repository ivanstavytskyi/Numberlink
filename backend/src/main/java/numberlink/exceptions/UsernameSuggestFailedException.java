package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameSuggestFailedException extends ApiException {
    public UsernameSuggestFailedException() {
        super(HttpStatus.SERVICE_UNAVAILABLE, "USERNAME_SUGGEST_FAILED", "Unable to suggest a username");
    }
}
