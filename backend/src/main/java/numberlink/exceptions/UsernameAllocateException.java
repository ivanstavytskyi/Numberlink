package numberlink.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameAllocateException extends ApiException {
    public UsernameAllocateException() {
        super(HttpStatus.CONFLICT, "USERNAME_ALLOCATE_FAILED", "Unable to allocate username");
    }
}
