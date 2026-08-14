package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyVerifiedException extends ApiException {
    public EmailAlreadyVerifiedException() {
        super(HttpStatus.CONFLICT, "EMAIL_ALREADY_VERIFIED", "Email is already verified");
    }
}
