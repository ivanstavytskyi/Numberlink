package numberlink.config.security;

import numberlink.service.auth.UserSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class UserSessionFilter extends OncePerRequestFilter {

    private final UserSessionService userSessionService;

    public UserSessionFilter(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null
                || "anonymousUser".equals(authentication.getPrincipal())) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID userId;
        try {
            userId = UUID.fromString(authentication.getName());
        } catch (IllegalArgumentException ex) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!userSessionService.ensureActive(userId, request)) {
            invalidate(request.getSession(false));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static void invalidate(HttpSession session) {
        SecurityContextHolder.clearContext();
        if (session == null) {
            return;
        }
        try {
            session.invalidate();
        } catch (IllegalStateException ignored) {
            /* already invalid */
        }
    }
}
