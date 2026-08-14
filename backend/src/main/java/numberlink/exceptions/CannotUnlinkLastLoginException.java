package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class CannotUnlinkLastLoginException extends ApiException {
    public CannotUnlinkLastLoginException() {
        super(
                HttpStatus.BAD_REQUEST,
                "CANNOT_UNLINK_LAST_LOGIN",
                "Add a password or another sign-in method before disconnecting this account."
        );
    }
}
