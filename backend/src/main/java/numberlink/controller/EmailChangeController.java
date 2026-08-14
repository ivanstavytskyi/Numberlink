package numberlink.controller;

import numberlink.dto.user.email.request.ConfirmEmailChangeRequestDto;
import numberlink.dto.user.email.request.StartEmailChangeRequestDto;
import numberlink.service.mail.VerificationEmailComposer;
import numberlink.service.user.EmailChangeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class EmailChangeController {

    private final EmailChangeService emailChangeService;
    private final VerificationEmailComposer verificationEmailComposer;

    public EmailChangeController(
            EmailChangeService emailChangeService,
            VerificationEmailComposer verificationEmailComposer
    ) {
        this.emailChangeService = emailChangeService;
        this.verificationEmailComposer = verificationEmailComposer;
    }

    @PostMapping("/api/me/email-change")
    public ResponseEntity<?> start(@Valid @RequestBody StartEmailChangeRequestDto dto) {
        return ResponseEntity.ok(emailChangeService.start(dto));
    }

    @PostMapping("/api/me/email-change/confirm")
    public ResponseEntity<?> confirm(
            @Valid @RequestBody ConfirmEmailChangeRequestDto dto,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(emailChangeService.confirm(dto, request));
    }

    @PostMapping("/api/me/email-change/resend")
    public ResponseEntity<?> resend() {
        return ResponseEntity.ok(emailChangeService.resend());
    }

    @PostMapping("/api/me/email-change/cancel")
    public ResponseEntity<?> cancelFromSettings() {
        emailChangeService.cancelFromSettings();
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/api/email-change/cancel")
    public ResponseEntity<Void> cancelFromLink(@RequestParam(required = false) String token) {
        boolean cancelled = emailChangeService.cancelFromToken(token);
        String flag = cancelled ? "cancelled" : "invalid";
        String location = verificationEmailComposer.frontendBaseUrl()
                + "/?emailChange="
                + UriUtils.encodeQueryParam(flag, StandardCharsets.UTF_8);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(location)).build();
    }
}
