package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class EmailChangeResendTooSoonException extends ApiException {
    public EmailChangeResendTooSoonException() {
        super(HttpStatus.BAD_REQUEST, "EMAIL_CHANGE_RESEND_TOO_SOON", "Wait 60 seconds before requesting another code.");
    }
}
