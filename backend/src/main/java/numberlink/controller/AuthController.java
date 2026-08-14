package numberlink.controller;

import numberlink.dto.mail.request.ResendVerificationRequestDto;
import numberlink.dto.mail.request.VerifyEmailRequestDto;
import numberlink.dto.user.login.request.LoginRequestDto;
import numberlink.dto.user.password.request.ForgotPasswordRequestDto;
import numberlink.dto.user.password.request.ResetPasswordRequestDto;
import numberlink.dto.user.register.request.RegisterRequestDto;
import numberlink.dto.user.totp.request.TotpCodeRequestDto;
import numberlink.entity.UserEntity;
import numberlink.service.auth.AuthService;
import numberlink.service.auth.OauthService;
import numberlink.service.auth.TotpService;
import numberlink.service.user.EmailChangeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final EmailChangeService emailChangeService;
    private final OauthService oauthService;
    private final TotpService totpService;

    public AuthController(
            AuthService authService,
            EmailChangeService emailChangeService,
            OauthService oauthService,
            TotpService totpService
    ) {
        this.authService = authService;
        this.emailChangeService = emailChangeService;
        this.oauthService = oauthService;
        this.totpService = totpService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequestDto dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserEntity user = authService.register(dto, request, response);
        Map<String, Object> payload = userPayload(user);
        payload.put("verificationRequired", true);
        return ResponseEntity.status(HttpStatus.CREATED).body(payload);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDto dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        AuthService.LoginOutcome outcome = authService.login(dto, request, response);
        if (outcome.twoFactorRequired()) {
            return ResponseEntity.ok(Map.of("twoFactorRequired", true));
        }
        UserEntity user = outcome.user();
        Map<String, Object> payload = userPayload(user);
        emailChangeService.appendProfileFlags(payload, user);
        oauthService.appendAccounts(payload, user);
        totpService.appendFlag(payload, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(payload);
    }

    @PostMapping("/login/2fa")
    public ResponseEntity<?> loginTwoFactor(
            @Valid @RequestBody TotpCodeRequestDto dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserEntity user = authService.completeTwoFactorLogin(dto.code(), request, response);
        Map<String, Object> payload = userPayload(user);
        emailChangeService.appendProfileFlags(payload, user);
        oauthService.appendAccounts(payload, user);
        totpService.appendFlag(payload, user);
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@Valid @RequestBody ResendVerificationRequestDto dto) {
        authService.resendVerificationEmail(dto.email());
        return ResponseEntity.accepted().body(Map.of("ok", true));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(
            @Valid @RequestBody VerifyEmailRequestDto dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserEntity user = authService.verifyEmail(dto.token(), request, response);
        Map<String, Object> payload = userPayload(user);
        emailChangeService.appendProfileFlags(payload, user);
        oauthService.appendAccounts(payload, user);
        totpService.appendFlag(payload, user);
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto dto) {
        authService.requestPasswordReset(dto.email());
        return ResponseEntity.accepted().body(Map.of(
                "ok", true,
                "message", "If an account exists for that email, we sent a reset link."
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto dto) {
        authService.resetPassword(dto.token(), dto.password());
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "message", "Password updated. You can log in with your new password."
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        UserEntity user = authService.requireCurrentUser();
        Map<String, Object> payload = userPayload(user);
        emailChangeService.appendProfileFlags(payload, user);
        oauthService.appendAccounts(payload, user);
        totpService.appendFlag(payload, user);
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/generate-name")
    public ResponseEntity<?> generateName() {
        return ResponseEntity.ok(Map.of("username", authService.suggestUsername()));
    }

    private static Map<String, Object> userPayload(UserEntity user) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", user.getId());
        payload.put("username", user.getUsername());
        payload.put("email", user.getEmail());
        payload.put("emailVerified", user.isEmailVerified());
        payload.put("avatarUrl", user.getAvatarUrl());
        return payload;
    }
}
