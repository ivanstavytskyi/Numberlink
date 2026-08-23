package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class AvatarTooLargeException extends ApiException {
    public AvatarTooLargeException() {
        super(HttpStatus.CONTENT_TOO_LARGE, "AVATAR_TOO_LARGE", "Avatar must be 1 MB or smaller");
    }
}
