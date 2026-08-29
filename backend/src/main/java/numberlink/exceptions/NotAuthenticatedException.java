package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class NotAuthenticatedException extends ApiException {
    public NotAuthenticatedException() {
        super(HttpStatus.UNAUTHORIZED, "NOT_AUTHENTICATED", "Not authenticated");
    }
}
