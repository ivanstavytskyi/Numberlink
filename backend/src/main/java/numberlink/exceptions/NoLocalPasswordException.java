package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class NoLocalPasswordException extends ApiException {
    public NoLocalPasswordException() {
        super(
                HttpStatus.BAD_REQUEST,
                "NO_LOCAL_PASSWORD",
                "This account signs in with Google or GitHub and does not have a password yet."
        );
    }
}
