package numberlink.config.security;

import numberlink.repository.UserRepository;
import numberlink.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class OauthLoginFailureHandler implements AuthenticationFailureHandler {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final String frontendBaseUrl;
    private final String loginFailureUrl;

    public OauthLoginFailureHandler(
            AuthService authService,
            UserRepository userRepository,
            @Value("${app.frontend-base-url:http://localhost:7000}") String frontendBaseUrl
    ) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.frontendBaseUrl = frontendBaseUrl;
        String base = frontendBaseUrl == null ? "http://localhost:7000" : frontendBaseUrl.trim();
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        this.loginFailureUrl = base + "/?login=failed";
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        HttpSession session = request.getSession(false);
        UUID linkUserId = session == null ? null : (UUID) session.getAttribute(OauthLinkSession.USER_ID);
        String returnTo = session == null ? "/" : (String) session.getAttribute(OauthLinkSession.RETURN_TO);
        if (session != null) {
            session.removeAttribute(OauthLinkSession.USER_ID);
            session.removeAttribute(OauthLinkSession.RETURN_TO);
        }

        if (linkUserId != null) {
            userRepository.findById(linkUserId)
                    .ifPresent(user -> authService.establishSession(user, request, response));
            response.sendRedirect(OauthLinkSession.redirectUrl(frontendBaseUrl, returnTo, "failed"));
            return;
        }

        response.sendRedirect(loginFailureUrl);
    }
}
