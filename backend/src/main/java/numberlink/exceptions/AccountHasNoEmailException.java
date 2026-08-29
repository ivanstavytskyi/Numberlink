package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class AccountHasNoEmailException extends ApiException {
    public AccountHasNoEmailException() {
        super(HttpStatus.BAD_REQUEST, "ACCOUNT_HAS_NO_EMAIL", "Account has no email");
    }
}
