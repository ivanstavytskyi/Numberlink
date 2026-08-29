package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class EmailTakenException extends ApiException {
    public EmailTakenException() {
        super(HttpStatus.CONFLICT, "EMAIL_TAKEN", "Email already taken");
    }
}
