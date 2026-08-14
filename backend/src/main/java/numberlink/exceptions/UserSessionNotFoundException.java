package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class UserSessionNotFoundException extends ApiException {
    public UserSessionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "SESSION_NOT_FOUND", "That session is no longer active.");
    }
}
