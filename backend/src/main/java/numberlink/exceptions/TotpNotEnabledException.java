package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class TotpNotEnabledException extends ApiException {
    public TotpNotEnabledException() {
        super(HttpStatus.BAD_REQUEST, "TOTP_NOT_ENABLED", "Two-factor authentication is not on.");
    }
}
