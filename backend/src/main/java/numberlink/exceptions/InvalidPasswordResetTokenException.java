package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidPasswordResetTokenException extends ApiException {
    public InvalidPasswordResetTokenException() {
        super(HttpStatus.BAD_REQUEST, "INVALID_RESET_TOKEN", "This password reset link is invalid or has expired");
    }
}
