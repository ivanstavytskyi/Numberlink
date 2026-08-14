package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidEmailChangeCodeException extends ApiException {
    public InvalidEmailChangeCodeException(int attemptsLeft) {
        super(
                HttpStatus.BAD_REQUEST,
                "INVALID_EMAIL_CHANGE_CODE",
                attemptsLeft > 0
                        ? "Incorrect code. " + attemptsLeft + (attemptsLeft == 1 ? " attempt" : " attempts") + " left."
                        : "Too many incorrect codes. Start the email change again."
        );
    }
}
