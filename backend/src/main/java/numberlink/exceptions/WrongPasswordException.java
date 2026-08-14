package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class WrongPasswordException extends ApiException {
    public WrongPasswordException() {
        super(HttpStatus.BAD_REQUEST, "WRONG_PASSWORD", "That password is incorrect.");
    }
}
