package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameTakenException extends ApiException {
    public UsernameTakenException() {
        super(HttpStatus.CONFLICT, "USERNAME_TAKEN", "Username already taken");
    }
}
