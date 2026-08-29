package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class EmailSendException extends ApiException {
    public EmailSendException(String message) {
        super(HttpStatus.BAD_GATEWAY, "EMAIL_SEND_FAILED", message);
    }

    public EmailSendException(String message, Throwable cause) {
        super(HttpStatus.BAD_GATEWAY, "EMAIL_SEND_FAILED", message, cause);
    }
}
