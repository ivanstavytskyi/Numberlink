package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class TotpSetupRequiredException extends ApiException {
    public TotpSetupRequiredException() {
        super(HttpStatus.BAD_REQUEST, "TOTP_SETUP_REQUIRED", "Start two-factor setup before entering a code.");
    }
}
