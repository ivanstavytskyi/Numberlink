package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class PasswordUnchangedException extends ApiException {
    public PasswordUnchangedException() {
        super(HttpStatus.BAD_REQUEST, "PASSWORD_UNCHANGED", "Choose a different password from the current one.");
    }
}
