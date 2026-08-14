package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class CommentException extends ApiException {
    public CommentException(String message) {
        super(HttpStatus.BAD_REQUEST, "COMMENT_ERROR", message);
    }

    public CommentException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, "COMMENT_ERROR", message, cause);
    }
}
