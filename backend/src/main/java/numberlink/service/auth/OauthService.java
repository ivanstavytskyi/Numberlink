package numberlink.service.auth;

import numberlink.dto.user.oauth.response.OauthAccountDto;
import numberlink.entity.OauthUserEntity;
import numberlink.entity.UserEntity;
import numberlink.entity.enums.OauthProvider;
import numberlink.exceptions.CannotUnlinkLastLoginException;
import numberlink.exceptions.IncompleteOauthProfileException;
import numberlink.exceptions.OauthAccountNotLinkedException;
import numberlink.exceptions.OauthAccountTakenException;
import numberlink.exceptions.OauthAlreadyLinkedException;
import numberlink.exceptions.UnsupportedOauthProviderException;
import numberlink.exceptions.UsernameAllocateException;
import numberlink.repository.LocalUserRepository;
import numberlink.repository.OauthUserRepository;
import numberlink.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Google/GitHub login, registration, and account linking.
 */
@Service
@Transactional
public class OauthService {

    private final UserRepository userRepository;
    private final OauthUserRepository oauthUserRepository;
    private final LocalUserRepository localUserRepository;

    public OauthService(
            UserRepository userRepository,
            OauthUserRepository oauthUserRepository,
            LocalUserRepository localUserRepository
    ) {
        this.userRepository = userRepository;
        this.oauthUserRepository = oauthUserRepository;
        this.localUserRepository = localUserRepository;
    }

    public UserEntity loginOrRegister(OAuth2User oauthUser, String registrationId) {
        OauthProvider provider = toProvider(registrationId);
        ParsedProfile profile = parseProfile(oauthUser, provider);

        Optional<OauthUserEntity> existingLink =
                oauthUserRepository.findByProviderAndSub(provider, profile.sub());
        if (existingLink.isPresent()) {
            applyProfile(existingLink.get(), profile);
            return existingLink.get().getUser();
        }

        UserEntity user = resolveUser(profile);
        saveLink(user, provider, profile);
        return user;
    }

    public void linkAccount(UUID userId, OAuth2User oauthUser, String registrationId) {
        OauthProvider provider = toProvider(registrationId);
        ParsedProfile profile = parseProfile(oauthUser, provider);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IncompleteOauthProfileException("Account no longer exists"));

        Optional<OauthUserEntity> existingBySub =
                oauthUserRepository.findByProviderAndSub(provider, profile.sub());
        if (existingBySub.isPresent()) {
            OauthUserEntity link = existingBySub.get();
            if (link.getUser().getId().equals(userId)) {
                applyProfile(link, profile);
                return;
            }
            throw new OauthAccountTakenException(provider);
        }

        Optional<OauthUserEntity> existingByProvider =
                oauthUserRepository.findByUser_IdAndProvider(userId, provider);
        if (existingByProvider.isPresent()) {
            throw new OauthAlreadyLinkedException(provider);
        }

        saveLink(user, provider, profile);
    }

    public void unlinkAccount(UUID userId, String providerName) {
        OauthProvider provider = toProvider(providerName);
        OauthUserEntity link = oauthUserRepository.findByUser_IdAndProvider(userId, provider)
                .orElseThrow(() -> new OauthAccountNotLinkedException(provider));

        boolean hasPassword = localUserRepository.findByUserId(userId).isPresent();
        long oauthCount = oauthUserRepository.countByUser_Id(userId);
        if (!hasPassword && oauthCount <= 1) {
            throw new CannotUnlinkLastLoginException();
        }

        oauthUserRepository.delete(link);
    }

    public void prepareLink(UUID userId, String providerName) {
        OauthProvider provider = toProvider(providerName);
        if (oauthUserRepository.existsByUser_IdAndProvider(userId, provider)) {
            throw new OauthAlreadyLinkedException(provider);
        }
    }

    public List<OauthAccountDto> listAccounts(UserEntity user) {
        return oauthUserRepository.findByUser_Id(user.getId()).stream()
                .sorted(Comparator.comparing(link -> link.getProvider().name()))
                .map(link -> {
                    String displayName = StringUtils.hasText(link.getDisplayName())
                            ? link.getDisplayName()
                            : user.getUsername();
                    String email = StringUtils.hasText(link.getEmail())
                            ? link.getEmail()
                            : (link.getProvider() == OauthProvider.GOOGLE ? blankToNull(user.getEmail()) : null);
                    return new OauthAccountDto(link.getProvider().name(), displayName, email);
                })
                .toList();
    }

    public void appendAccounts(Map<String, Object> payload, UserEntity user) {
        payload.put("oauthAccounts", listAccounts(user));
    }

    public static OauthProvider toProvider(String registrationId) {
        if (registrationId == null || registrationId.isBlank()) {
            throw new UnsupportedOauthProviderException("unknown");
        }
        return switch (registrationId.toLowerCase(Locale.ROOT)) {
            case "google" -> OauthProvider.GOOGLE;
            case "github" -> OauthProvider.GITHUB;
            default -> throw new UnsupportedOauthProviderException(registrationId);
        };
    }

    private void saveLink(UserEntity user, OauthProvider provider, ParsedProfile profile) {
        OauthUserEntity link = new OauthUserEntity();
        link.setUser(user);
        link.setProvider(provider);
        link.setSub(profile.sub());
        link.setJoinedAt(Instant.now());
        applyProfile(link, profile);
        try {
            oauthUserRepository.saveAndFlush(link);
        } catch (DataIntegrityViolationException ex) {
            throw new OauthAccountTakenException(provider);
        }
    }

    private static void applyProfile(OauthUserEntity link, ParsedProfile profile) {
        link.setDisplayName(truncate(profile.displayName(), 255));
        link.setEmail(blankToNull(profile.email()));
    }

    private UserEntity resolveUser(ParsedProfile profile) {
        if (profile.email() == null || profile.email().isBlank()) {
            return createUser(profile);
        }
        return userRepository.findByEmailIgnoreCase(profile.email())
                .orElseGet(() -> createUser(profile));
    }

    private UserEntity createUser(ParsedProfile profile) {
        UserEntity user = new UserEntity();
        if (profile.email() != null && !profile.email().isBlank()) {
            user.setEmail(profile.email());
        }
        user.setUsername(uniqueUsername(profile.username()));
        user.setCreatedAt(Instant.now());
        return userRepository.saveAndFlush(user);
    }

    private String uniqueUsername(String hint) {
        String base = sanitizeUsername(hint);
        if (base.length() < 3) {
            base = "player";
        }
        if (!userRepository.existsByUsernameIgnoreCase(base)) {
            return base;
        }
        for (int n = 2; n < Integer.MAX_VALUE; n++) {
            String suffix = String.valueOf(n);
            String candidate = truncate(base, 32 - suffix.length()) + suffix;
            if (!userRepository.existsByUsernameIgnoreCase(candidate)) {
                return candidate;
            }
        }
        throw new UsernameAllocateException();
    }

    private static String sanitizeUsername(String raw) {
        if (raw == null || raw.isBlank()) {
            return "player";
        }
        String cleaned = raw.trim()
                .replaceAll("[^a-zA-Z0-9_]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
        return truncate(cleaned.isBlank() ? "player" : cleaned, 32);
    }

    private static String truncate(String value, int max) {
        if (value == null) {
            return null;
        }
        return value.length() <= max ? value : value.substring(0, max);
    }

    private static String blankToNull(String value) {
        return StringUtils.hasText(value) ? value.trim().toLowerCase(Locale.ROOT) : null;
    }

    private static ParsedProfile parseProfile(OAuth2User user, OauthProvider provider) {
        Map<String, Object> attrs = user.getAttributes();
        return switch (provider) {
            case GOOGLE -> parseGoogle(attrs);
            case GITHUB -> parseGithub(attrs);
        };
    }

    private static ParsedProfile parseGoogle(Map<String, Object> attrs) {
        String sub = stringAttr(attrs, "sub");
        String email = stringAttr(attrs, "email");
        if (sub == null || sub.isBlank()) {
            throw new IncompleteOauthProfileException("Google profile missing sub");
        }
        if (email == null || email.isBlank()) {
            throw new IncompleteOauthProfileException("Google profile missing email");
        }
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        String displayName = firstNonBlank(
                stringAttr(attrs, "name"),
                stringAttr(attrs, "given_name"),
                emailLocalPart(normalizedEmail)
        );
        String username = firstNonBlank(emailLocalPart(normalizedEmail), stringAttr(attrs, "given_name"), "google_" + sub);
        return new ParsedProfile(sub, normalizedEmail, username, displayName);
    }

    private static String emailLocalPart(String email) {
        if (email == null) {
            return null;
        }
        int at = email.indexOf('@');
        if (at <= 0) {
            return null;
        }
        return email.substring(0, at);
    }

    private static ParsedProfile parseGithub(Map<String, Object> attrs) {
        Object id = attrs.get("id");
        if (id == null) {
            throw new IncompleteOauthProfileException("GitHub profile missing id");
        }
        String sub = String.valueOf(id);
        String login = stringAttr(attrs, "login");
        String email = stringAttr(attrs, "email");
        String displayName = firstNonBlank(stringAttr(attrs, "name"), login, "GitHub");
        String username = firstNonBlank(login, stringAttr(attrs, "name"), "github_" + sub);
        String normalizedEmail = StringUtils.hasText(email) ? email.trim().toLowerCase(Locale.ROOT) : null;
        return new ParsedProfile(sub, normalizedEmail, username, displayName);
    }

    private static String stringAttr(Map<String, Object> attrs, String key) {
        Object value = attrs.get(key);
        return value == null ? null : String.valueOf(value);
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "player";
    }

    private record ParsedProfile(String sub, String email, String username, String displayName) {}
}
