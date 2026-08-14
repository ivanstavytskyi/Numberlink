package numberlink.config.security;

import numberlink.entity.UserEntity;
import numberlink.exceptions.OauthAccountTakenException;
import numberlink.exceptions.OauthAlreadyLinkedException;
import numberlink.repository.UserRepository;
import numberlink.service.auth.AuthService;
import numberlink.service.auth.OauthService;
import numberlink.service.auth.TotpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * After Google/GitHub callback: login/register, or attach the provider to the signed-in user.
 */
@Component
public class OauthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OauthService oauthService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final TotpService totpService;
    private final String frontendBaseUrl;

    public OauthLoginSuccessHandler(
            OauthService oauthService,
            AuthService authService,
            UserRepository userRepository,
            TotpService totpService,
            @Value("${app.oauth.success-redirect-url:http://localhost:7000/}") String successRedirectUrl,
            @Value("${app.frontend-base-url:http://localhost:7000}") String frontendBaseUrl
    ) {
        this.oauthService = oauthService;
        this.authService = authService;
        this.userRepository = userRepository;
        this.totpService = totpService;
        this.frontendBaseUrl = frontendBaseUrl;
        setDefaultTargetUrl(successRedirectUrl);
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        HttpSession session = request.getSession(false);
        UUID linkUserId = session == null ? null : (UUID) session.getAttribute(OauthLinkSession.USER_ID);
        String returnTo = session == null ? "/" : (String) session.getAttribute(OauthLinkSession.RETURN_TO);
        if (session != null) {
            session.removeAttribute(OauthLinkSession.USER_ID);
            session.removeAttribute(OauthLinkSession.RETURN_TO);
        }

        OAuth2User oauthUser = oauthToken.getPrincipal();
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        if (linkUserId != null) {
            String result = "ok";
            try {
                oauthService.linkAccount(linkUserId, oauthUser, registrationId);
            } catch (OauthAccountTakenException ex) {
                result = "taken";
            } catch (OauthAlreadyLinkedException ex) {
                result = "already";
            } catch (RuntimeException ex) {
                result = "failed";
            }
            restoreUser(linkUserId, request, response);
            getRedirectStrategy().sendRedirect(
                    request,
                    response,
                    OauthLinkSession.redirectUrl(frontendBaseUrl, returnTo, result)
            );
            return;
        }

        UserEntity user = oauthService.loginOrRegister(oauthUser, registrationId);
        if (totpService.isEnabled(user.getId())) {
            authService.clearAuthentication(request, response);
            authService.beginTwoFactorChallenge(user, request);
            getRedirectStrategy().sendRedirect(request, response, twoFactorRedirectUrl());
            return;
        }
        authService.establishSession(user, request, response);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private String twoFactorRedirectUrl() {
        String base = frontendBaseUrl == null ? "http://localhost:7000" : frontendBaseUrl.trim();
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/?login=2fa";
    }

    private void restoreUser(UUID userId, HttpServletRequest request, HttpServletResponse response) {
        userRepository.findById(userId).ifPresent(user -> authService.establishSession(user, request, response));
    }
}
