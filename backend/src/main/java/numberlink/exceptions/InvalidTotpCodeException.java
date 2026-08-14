package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTotpCodeException extends ApiException {
    public InvalidTotpCodeException() {
        super(HttpStatus.BAD_REQUEST, "INVALID_TOTP_CODE", "That code is incorrect or expired.");
    }
}
