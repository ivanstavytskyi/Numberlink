package numberlink.service.auth;

import numberlink.dto.user.session.response.UserSessionResponseDto;
import numberlink.entity.UserEntity;
import numberlink.entity.UserSessionEntity;
import numberlink.exceptions.UserSessionNotFoundException;
import numberlink.repository.UserRepository;
import numberlink.repository.UserSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserSessionService {

    public static final String USER_SESSION_ID_ATTR = "nl.userSessionId";

    private static final Duration LAST_SEEN_TOUCH_AFTER = Duration.ofMinutes(2);

    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;

    public UserSessionService(
            UserSessionRepository userSessionRepository,
            UserRepository userRepository
    ) {
        this.userSessionRepository = userSessionRepository;
        this.userRepository = userRepository;
    }

    public void remember(UUID userId, HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return;
        }

        String hash = hashSessionId(httpSession.getId());
        Instant now = Instant.now();
        UserAgentParser.Summary ua = UserAgentParser.parse(request.getHeader("User-Agent"));

        Optional<UserSessionEntity> existing = userSessionRepository.findByHttpSessionHash(hash);
        if (existing.isPresent()) {
            UserSessionEntity row = existing.get();
            if (!row.getUser().getId().equals(userId)) {
                return;
            }
            row.setRevokedAt(null);
            row.setDevice(ua.device());
            row.setOs(ua.os());
            row.setBrowser(ua.browser());
            row.setLastSeenAt(now);
            userSessionRepository.save(row);
            httpSession.setAttribute(USER_SESSION_ID_ATTR, row.getId());
            return;
        }

        UserEntity user = userRepository.getReferenceById(userId);
        UserSessionEntity row = new UserSessionEntity();
        row.setUser(user);
        row.setHttpSessionHash(hash);
        row.setDevice(ua.device());
        row.setOs(ua.os());
        row.setBrowser(ua.browser());
        row.setCreatedAt(now);
        row.setLastSeenAt(now);
        row = userSessionRepository.save(row);
        httpSession.setAttribute(USER_SESSION_ID_ATTR, row.getId());
    }

    /**
     * @return false when this HTTP session was revoked or belongs to another user
     */
    public boolean ensureActive(UUID userId, HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return true;
        }

        String hash = hashSessionId(httpSession.getId());
        Optional<UserSessionEntity> found = userSessionRepository.findByHttpSessionHash(hash);
        if (found.isEmpty()) {
            remember(userId, request);
            return true;
        }

        UserSessionEntity row = found.get();
        if (row.getRevokedAt() != null || !row.getUser().getId().equals(userId)) {
            return false;
        }

        httpSession.setAttribute(USER_SESSION_ID_ATTR, row.getId());
        Instant now = Instant.now();
        if (row.getLastSeenAt() == null || row.getLastSeenAt().isBefore(now.minus(LAST_SEEN_TOUCH_AFTER))) {
            row.setLastSeenAt(now);
            userSessionRepository.save(row);
        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponseDto> listActive(UUID userId, HttpServletRequest request) {
        UUID currentId = currentSessionId(request).orElse(null);
        return userSessionRepository.findByUser_IdAndRevokedAtIsNullOrderByLastSeenAtDesc(userId).stream()
                .map(row -> toDto(row, row.getId().equals(currentId)))
                .toList();
    }

    public void revoke(UUID userId, UUID sessionId, HttpServletRequest request) {
        UserSessionEntity row = userSessionRepository
                .findByIdAndUser_IdAndRevokedAtIsNull(sessionId, userId)
                .orElseThrow(UserSessionNotFoundException::new);
        row.setRevokedAt(Instant.now());
        userSessionRepository.save(row);

        currentSessionId(request).ifPresent(currentId -> {
            if (currentId.equals(sessionId)) {
                invalidate(request);
            }
        });
    }

    public void revokeOthers(UUID userId, HttpServletRequest request) {
        UUID keepId = currentSessionId(request).orElse(null);
        if (keepId == null) {
            userSessionRepository.revokeAll(userId, Instant.now());
            return;
        }
        userSessionRepository.revokeOthers(userId, keepId, Instant.now());
    }

    public void revokeAll(UUID userId) {
        userSessionRepository.revokeAll(userId, Instant.now());
    }

    public void revokeCurrent(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return;
        }
        userSessionRepository.findByHttpSessionHash(hashSessionId(httpSession.getId()))
                .filter(row -> row.getRevokedAt() == null)
                .ifPresent(row -> {
                    row.setRevokedAt(Instant.now());
                    userSessionRepository.save(row);
                });
    }

    private Optional<UUID> currentSessionId(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return Optional.empty();
        }
        Object stored = httpSession.getAttribute(USER_SESSION_ID_ATTR);
        if (stored instanceof UUID id) {
            return Optional.of(id);
        }
        return userSessionRepository.findByHttpSessionHash(hashSessionId(httpSession.getId()))
                .filter(row -> row.getRevokedAt() == null)
                .map(UserSessionEntity::getId);
    }

    private static void invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        try {
            session.invalidate();
        } catch (IllegalStateException ignored) {
            /* already invalid */
        }
    }

    static String hashSessionId(String sessionId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(sessionId.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is required", ex);
        }
    }

    private static UserSessionResponseDto toDto(UserSessionEntity row, boolean current) {
        return new UserSessionResponseDto(
                row.getId(),
                row.getDevice(),
                row.getOs(),
                row.getBrowser(),
                row.getLastSeenAt(),
                row.getCreatedAt(),
                current
        );
    }
}
