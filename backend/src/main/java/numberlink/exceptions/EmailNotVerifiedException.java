package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class EmailNotVerifiedException extends ApiException {

    private final String email;

    public EmailNotVerifiedException(String email) {
        super(HttpStatus.FORBIDDEN, "EMAIL_NOT_VERIFIED", "Confirm your email before logging in.");
        this.email = email == null ? "" : email;
    }

    public String getEmail() {
        return email;
    }
}
