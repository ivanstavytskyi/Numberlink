package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends ApiException {
    public AccountNotFoundException() {
        super(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND", "No account found for that email");
    }
}
