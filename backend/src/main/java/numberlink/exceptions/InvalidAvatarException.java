package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidAvatarException extends ApiException {
    public InvalidAvatarException(String message) {
        super(HttpStatus.BAD_REQUEST, "INVALID_AVATAR", message);
    }
}
