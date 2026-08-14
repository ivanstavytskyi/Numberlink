package numberlink.config.security;

import numberlink.repository.UserRepository;
import numberlink.service.auth.AuthService;
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
public class SessionEpochFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public SessionEpochFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        HttpSession session = request.getSession(false);
        if (session == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Integer dbEpoch = userRepository.findSessionEpochById(userId).orElse(null);
        if (dbEpoch == null) {
            invalidate(session);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Object stored = session.getAttribute(AuthService.SESSION_EPOCH_ATTR);
        if (stored == null) {
            session.setAttribute(AuthService.SESSION_EPOCH_ATTR, dbEpoch);
            filterChain.doFilter(request, response);
            return;
        }

        int sessionEpoch = stored instanceof Integer i ? i : Integer.parseInt(stored.toString());
        if (sessionEpoch != dbEpoch) {
            invalidate(session);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static void invalidate(HttpSession session) {
        SecurityContextHolder.clearContext();
        try {
            session.invalidate();
        } catch (IllegalStateException ignored) {
            /* already invalid */
        }
    }
}
