package numberlink.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Builds auth email HTML and deep links from classpath templates.
 */
@Component
public class VerificationEmailComposer {

    private final String confirmEmailHtml;
    private final String resetPasswordHtml;
    private final String emailChangeCodeHtml;
    private final String emailChangeCancelHtml;
    private final String frontendBaseUrl;
    private final String backendBaseUrl;

    public VerificationEmailComposer(
            @Value("${app.frontend-base-url:http://localhost:7000}") String frontendBaseUrl,
            @Value("${app.backend-base-url:http://localhost:8000}") String backendBaseUrl
    ) throws IOException {
        this.frontendBaseUrl = trimTrailingSlash(frontendBaseUrl);
        this.backendBaseUrl = trimTrailingSlash(backendBaseUrl);
        this.confirmEmailHtml = StreamUtils.copyToString(
                new ClassPathResource("mail/confirm-email.html").getInputStream(),
                StandardCharsets.UTF_8
        );
        this.resetPasswordHtml = StreamUtils.copyToString(
                new ClassPathResource("mail/reset-password.html").getInputStream(),
                StandardCharsets.UTF_8
        );
        this.emailChangeCodeHtml = StreamUtils.copyToString(
                new ClassPathResource("mail/email-change-code.html").getInputStream(),
                StandardCharsets.UTF_8
        );
        this.emailChangeCancelHtml = StreamUtils.copyToString(
                new ClassPathResource("mail/email-change-cancel.html").getInputStream(),
                StandardCharsets.UTF_8
        );
    }

    public String buildVerifyLink(String rawToken) {
        String encoded = UriUtils.encodeQueryParam(rawToken, StandardCharsets.UTF_8);
        return frontendBaseUrl + "/verify/?token=" + encoded;
    }

    public String buildPasswordResetLink(String rawToken) {
        String encoded = UriUtils.encodeQueryParam(rawToken, StandardCharsets.UTF_8);
        return frontendBaseUrl + "/?resetToken=" + encoded;
    }

    public String renderConfirmHtml(String username, String verifyUrl) {
        String safeName = HtmlUtils.htmlEscape(username == null ? "player" : username.trim());
        String safeUrl = HtmlUtils.htmlEscape(verifyUrl.trim());
        return confirmEmailHtml
                .replace("{{username}}", safeName)
                .replace("{{verifyUrl}}", safeUrl);
    }

    public String renderResetPasswordHtml(String username, String resetUrl, Duration ttl) {
        String safeName = HtmlUtils.htmlEscape(username == null ? "player" : username.trim());
        String safeUrl = HtmlUtils.htmlEscape(resetUrl.trim());
        long hours = Math.max(1, ttl.toHours());
        return resetPasswordHtml
                .replace("{{username}}", safeName)
                .replace("{{resetUrl}}", safeUrl)
                .replace("{{ttlHours}}", String.valueOf(hours));
    }

    public String buildEmailChangeCancelLink(String rawToken) {
        String encoded = UriUtils.encodeQueryParam(rawToken, StandardCharsets.UTF_8);
        return backendBaseUrl + "/api/email-change/cancel?token=" + encoded;
    }

    public String renderEmailChangeCodeHtml(String username, String code, int ttlMinutes) {
        String safeName = HtmlUtils.htmlEscape(username == null ? "player" : username.trim());
        String safeCode = HtmlUtils.htmlEscape(code);
        return emailChangeCodeHtml
                .replace("{{username}}", safeName)
                .replace("{{code}}", safeCode)
                .replace("{{ttlMinutes}}", String.valueOf(ttlMinutes));
    }

    public String renderEmailChangeCancelHtml(String username, String newEmail, String cancelUrl, int ttlMinutes) {
        String safeName = HtmlUtils.htmlEscape(username == null ? "player" : username.trim());
        String safeEmail = HtmlUtils.htmlEscape(newEmail == null ? "" : newEmail.trim());
        String safeUrl = HtmlUtils.htmlEscape(cancelUrl.trim());
        return emailChangeCancelHtml
                .replace("{{username}}", safeName)
                .replace("{{newEmail}}", safeEmail)
                .replace("{{cancelUrl}}", safeUrl)
                .replace("{{ttlMinutes}}", String.valueOf(ttlMinutes));
    }

    public String frontendBaseUrl() {
        return frontendBaseUrl;
    }

    private static String trimTrailingSlash(String url) {
        if (url == null || url.isBlank()) {
            return "http://localhost:7000";
        }
        String trimmed = url.trim();
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}
