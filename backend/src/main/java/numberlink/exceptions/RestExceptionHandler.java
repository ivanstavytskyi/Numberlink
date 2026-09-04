package numberlink.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", ex.getCode());
        body.put("message", ex.getMessage());
        body.put("status", ex.getStatus().value());

        if (ex instanceof EmailNotVerifiedException notVerified) {
            body.put("email", notVerified.getEmail());
        }

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation failed");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", "VALIDATION_ERROR");
        body.put("message", message);
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class, MultipartException.class})
    public ResponseEntity<Map<String, Object>> handleMaxUpload(Exception ex) {
        boolean tooLarge = ex instanceof MaxUploadSizeExceededException
                || ex.getCause() instanceof MaxUploadSizeExceededException;
        if (!tooLarge && !(ex instanceof MaxUploadSizeExceededException)) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("code", "INVALID_AVATAR");
            body.put("message", "Could not read uploaded file.");
            body.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(body);
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", "AVATAR_TOO_LARGE");
        body.put("message", "Avatar must be 7 MB or smaller");
        body.put("status", HttpStatus.CONTENT_TOO_LARGE.value());
        return ResponseEntity.status(HttpStatus.CONTENT_TOO_LARGE).body(body);
    }
}
