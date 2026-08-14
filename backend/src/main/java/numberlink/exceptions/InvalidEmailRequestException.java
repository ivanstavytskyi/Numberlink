package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidEmailRequestException extends ApiException {
    public InvalidEmailRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, "INVALID_EMAIL_REQUEST", message);
    }
}
