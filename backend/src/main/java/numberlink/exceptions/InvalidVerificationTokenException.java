package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidVerificationTokenException extends ApiException {
    public InvalidVerificationTokenException() {
        super(
                HttpStatus.BAD_REQUEST,
                "INVALID_VERIFICATION_TOKEN",
                "This confirmation link is invalid or has expired."
        );
    }
}
