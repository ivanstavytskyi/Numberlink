package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameOrEmailTakenException extends ApiException {
    public UsernameOrEmailTakenException() {
        super(HttpStatus.CONFLICT, "USERNAME_OR_EMAIL_TAKEN", "Username or email already taken");
    }
}
