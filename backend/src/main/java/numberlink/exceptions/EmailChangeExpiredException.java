package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class EmailChangeExpiredException extends ApiException {
    public EmailChangeExpiredException() {
        super(HttpStatus.BAD_REQUEST, "EMAIL_CHANGE_EXPIRED", "This email change request has expired. Start again.");
    }
}
