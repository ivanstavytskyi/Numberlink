package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class RatingException extends ApiException {
    public RatingException(String message) {
        super(HttpStatus.BAD_REQUEST, "RATING_ERROR", message);
    }

    public RatingException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, "RATING_ERROR", message, cause);
    }
}
