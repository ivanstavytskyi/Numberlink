package numberlink.controller;

import numberlink.entity.UserEntity;
import numberlink.service.auth.AuthService;
import numberlink.service.auth.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/me/sessions")
public class UserSessionController {

    private final AuthService authService;
    private final UserSessionService userSessionService;

    public UserSessionController(AuthService authService, UserSessionService userSessionService) {
        this.authService = authService;
        this.userSessionService = userSessionService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(HttpServletRequest request) {
        UserEntity user = authService.requireCurrentUser();
        userSessionService.remember(user.getId(), request);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("sessions", userSessionService.listActive(user.getId(), request));
        return ResponseEntity.ok(body);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> revokeOthers(HttpServletRequest request) {
        UserEntity user = authService.requireCurrentUser();
        userSessionService.revokeOthers(user.getId(), request);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("sessions", userSessionService.listActive(user.getId(), request));
        return ResponseEntity.ok(body);
    }

    @PostMapping("/revoke-all")
    public ResponseEntity<Void> revokeAll(HttpServletRequest request, HttpServletResponse response) {
        UserEntity user = authService.requireCurrentUser();
        userSessionService.revokeAll(user.getId());
        authService.logout(request, response);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> revoke(
            @PathVariable UUID id,
            HttpServletRequest request
    ) {
        UserEntity user = authService.requireCurrentUser();
        userSessionService.revoke(user.getId(), id, request);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("sessions", userSessionService.listActive(user.getId(), request));
        return ResponseEntity.ok(body);
    }
}
