package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class TotpAlreadyEnabledException extends ApiException {
    public TotpAlreadyEnabledException() {
        super(HttpStatus.CONFLICT, "TOTP_ALREADY_ENABLED", "Two-factor authentication is already on.");
    }
}
