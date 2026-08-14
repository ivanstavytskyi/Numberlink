package numberlink.service.mail;

import numberlink.exceptions.EmailSendException;
import numberlink.exceptions.InvalidEmailRequestException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final String fromName;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String fromAddress,
            @Value("${app.mail.from-name:NumberLink}") String fromName
    ) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress.trim();
        this.fromName = fromName == null ? "NumberLink" : fromName.trim();
    }

    public void sendText(String to, String subject, String body) {
        requireAddress(to, "to");
        requireText(subject, "subject");
        requireText(body, "body");

        String plain = body.strip();
        sendMime(to.trim(), subject.strip(), plain, toSimpleHtml(plain));
    }

    public void sendHtml(String to, String subject, String htmlBody) {
        requireAddress(to, "to");
        requireText(subject, "subject");
        requireText(htmlBody, "htmlBody");

        String html = htmlBody.strip();
        String plain = html
                .replaceAll("(?is)<br\\s*/?>", "\n")
                .replaceAll("(?is)</p>", "\n")
                .replaceAll("(?is)<[^>]+>", " ")
                .replace("&nbsp;", " ")
                .replaceAll("\\s+", " ")
                .strip();
        if (!StringUtils.hasText(plain)) {
            plain = " ";
        }
        sendMime(to.trim(), subject.strip(), plain, html);
    }

    private void sendMime(String to, String subject, String plainBody, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            InternetAddress from = new InternetAddress(fromAddress, fromName, StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            // Both parts required: some clients only render HTML, others only plain.
            helper.setText(plainBody, htmlBody);

            mailSender.send(message);
        } catch (MessagingException | MailException | java.io.UnsupportedEncodingException ex) {
            String detail = rootMessage(ex);
            throw new EmailSendException("Failed to send email to " + to + ": " + detail, ex);
        }
    }

    private static String toSimpleHtml(String plain) {
        String escaped = HtmlUtils.htmlEscape(plain);
        String withBreaks = escaped.replace("\r\n", "\n").replace("\n", "<br>\n");
        return "<html><body><p>" + withBreaks + "</p></body></html>";
    }

    private static String rootMessage(Throwable ex) {
        Throwable cur = ex;
        String last = ex.getMessage();
        while (cur != null) {
            if (StringUtils.hasText(cur.getMessage())) {
                last = cur.getMessage();
            }
            cur = cur.getCause();
        }
        return last == null ? ex.getClass().getSimpleName() : last;
    }

    private static void requireAddress(String value, String field) {
        if (!StringUtils.hasText(value) || !value.contains("@")) {
            throw new InvalidEmailRequestException("Invalid " + field + " address");
        }
    }

    private static void requireText(String value, String field) {
        if (!StringUtils.hasText(value)) {
            throw new InvalidEmailRequestException(field + " must not be blank");
        }
    }
}
