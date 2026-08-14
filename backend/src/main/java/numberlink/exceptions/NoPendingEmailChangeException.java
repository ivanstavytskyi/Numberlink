package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class NoPendingEmailChangeException extends ApiException {
    public NoPendingEmailChangeException() {
        super(HttpStatus.BAD_REQUEST, "NO_PENDING_EMAIL_CHANGE", "There is no email change in progress.");
    }
}
