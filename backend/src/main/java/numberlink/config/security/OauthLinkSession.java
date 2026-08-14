package numberlink.config.security;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class OauthLinkSession {
    public static final String USER_ID = "nl.oauthLinkUserId";
    public static final String RETURN_TO = "nl.oauthLinkReturnTo";

    private OauthLinkSession() {
    }

    public static String sanitizeReturnTo(String raw) {
        if (raw == null || raw.isBlank()) {
            return "/";
        }
        String path = raw.trim();
        if (!path.startsWith("/") || path.startsWith("//") || path.contains("://")) {
            return "/";
        }
        if (path.indexOf('\n') >= 0 || path.indexOf('\r') >= 0) {
            return "/";
        }
        return path;
    }

    public static String redirectUrl(String frontendBaseUrl, String returnTo, String result) {
        String base = frontendBaseUrl == null ? "http://localhost:7000" : frontendBaseUrl.trim();
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        String path = sanitizeReturnTo(returnTo);
        String hash = "";
        int hashAt = path.indexOf('#');
        if (hashAt >= 0) {
            hash = path.substring(hashAt);
            path = path.substring(0, hashAt);
        }
        String sep = path.contains("?") ? "&" : "?";
        return base + path + sep + "oauthLink=" + URLEncoder.encode(result.toLowerCase(Locale.ROOT), StandardCharsets.UTF_8) + hash;
    }
}
