package numberlink.controller;

import numberlink.dto.user.totp.request.TotpCodeRequestDto;
import numberlink.entity.UserEntity;
import numberlink.service.auth.AuthService;
import numberlink.service.auth.TotpService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/me/2fa")
public class TotpController {

    private final AuthService authService;
    private final TotpService totpService;

    public TotpController(AuthService authService, TotpService totpService) {
        this.authService = authService;
        this.totpService = totpService;
    }

    @PostMapping("/setup")
    public ResponseEntity<Map<String, Object>> setup() {
        UserEntity user = authService.requireCurrentUser();
        return ResponseEntity.ok(totpService.startSetup(user));
    }

    @DeleteMapping("/setup")
    public ResponseEntity<Map<String, Object>> cancelSetup() {
        UserEntity user = authService.requireCurrentUser();
        totpService.cancelSetup(user);
        return ResponseEntity.ok(Map.of("ok", true, "twoFactorEnabled", false));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Map<String, Object>> confirm(@Valid @RequestBody TotpCodeRequestDto dto) {
        UserEntity user = authService.requireCurrentUser();
        totpService.confirmSetup(user, dto.code());
        return ResponseEntity.ok(Map.of("ok", true, "twoFactorEnabled", true));
    }

    @PostMapping("/disable")
    public ResponseEntity<Map<String, Object>> disable(@Valid @RequestBody TotpCodeRequestDto dto) {
        UserEntity user = authService.requireCurrentUser();
        totpService.disable(user, dto.code());
        return ResponseEntity.ok(Map.of("ok", true, "twoFactorEnabled", false));
    }
}
