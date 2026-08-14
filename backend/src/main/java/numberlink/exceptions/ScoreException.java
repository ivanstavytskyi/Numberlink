package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class ScoreException extends ApiException {
    public ScoreException(String message) {
        super(HttpStatus.BAD_REQUEST, "SCORE_ERROR", message);
    }

    public ScoreException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, "SCORE_ERROR", message, cause);
    }
}
