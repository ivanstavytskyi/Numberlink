package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class EmailChangeNotAllowedException extends ApiException {
    public EmailChangeNotAllowedException(String message) {
        super(HttpStatus.FORBIDDEN, "EMAIL_CHANGE_NOT_ALLOWED", message);
    }
}
