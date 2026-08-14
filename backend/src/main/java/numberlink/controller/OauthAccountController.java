package numberlink.controller;

import numberlink.config.security.OauthLinkSession;
import numberlink.dto.user.oauth.request.PrepareOauthLinkRequestDto;
import numberlink.entity.UserEntity;
import numberlink.service.auth.AuthService;
import numberlink.service.auth.OauthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/me/oauth")
public class OauthAccountController {

    private final AuthService authService;
    private final OauthService oauthService;

    public OauthAccountController(AuthService authService, OauthService oauthService) {
        this.authService = authService;
        this.oauthService = oauthService;
    }

    @PostMapping("/prepare-link")
    public ResponseEntity<Map<String, Object>> prepareLink(
            @Valid @RequestBody PrepareOauthLinkRequestDto dto,
            HttpServletRequest request
    ) {
        UserEntity user = authService.requireCurrentUser();
        oauthService.prepareLink(user.getId(), dto.provider());

        HttpSession session = request.getSession(true);
        session.setAttribute(OauthLinkSession.USER_ID, user.getId());
        session.setAttribute(OauthLinkSession.RETURN_TO, OauthLinkSession.sanitizeReturnTo(dto.returnTo()));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("provider", OauthService.toProvider(dto.provider()).name().toLowerCase());
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{provider}")
    public ResponseEntity<Map<String, Object>> unlink(@PathVariable String provider) {
        UserEntity user = authService.requireCurrentUser();
        oauthService.unlinkAccount(user.getId(), provider);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("ok", true);
        body.put("oauthAccounts", oauthService.listAccounts(user));
        return ResponseEntity.ok(body);
    }
}
