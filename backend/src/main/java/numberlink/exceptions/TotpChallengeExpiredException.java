package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class TotpChallengeExpiredException extends ApiException {
    public TotpChallengeExpiredException() {
        super(HttpStatus.UNAUTHORIZED, "TOTP_CHALLENGE_EXPIRED", "That sign-in expired. Log in again.");
    }
}
