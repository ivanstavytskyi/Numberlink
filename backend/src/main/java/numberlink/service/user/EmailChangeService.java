package numberlink.service.user;

import numberlink.dto.user.email.request.ConfirmEmailChangeRequestDto;
import numberlink.dto.user.email.request.StartEmailChangeRequestDto;
import numberlink.entity.EmailChangeRequestEntity;
import numberlink.entity.LocalUserEntity;
import numberlink.entity.UserEntity;
import numberlink.entity.enums.OauthProvider;
import numberlink.exceptions.EmailChangeExpiredException;
import numberlink.exceptions.EmailChangeNotAllowedException;
import numberlink.exceptions.EmailChangeResendTooSoonException;
import numberlink.exceptions.EmailSendException;
import numberlink.exceptions.EmailTakenException;
import numberlink.exceptions.InvalidEmailChangeCodeException;
import numberlink.exceptions.NoPendingEmailChangeException;
import numberlink.exceptions.WrongPasswordException;
import numberlink.repository.EmailChangeRequestRepository;
import numberlink.repository.LocalUserRepository;
import numberlink.repository.OauthUserRepository;
import numberlink.repository.UserRepository;
import numberlink.service.auth.AuthService;
import numberlink.service.mail.EmailService;
import numberlink.service.mail.VerificationEmailComposer;
import numberlink.service.mail.VerificationTokenFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailChangeService {

    private static final int MAX_ATTEMPTS = 5;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final AuthService authService;
    private final UserRepository userRepository;
    private final LocalUserRepository localUserRepository;
    private final OauthUserRepository oauthUserRepository;
    private final EmailChangeRequestRepository emailChangeRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationEmailComposer verificationEmailComposer;
    private final Duration changeTtl;
    private final Duration resendCooldown;

    public EmailChangeService(
            AuthService authService,
            UserRepository userRepository,
            LocalUserRepository localUserRepository,
            OauthUserRepository oauthUserRepository,
            EmailChangeRequestRepository emailChangeRequestRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            VerificationEmailComposer verificationEmailComposer,
            @Value("${app.mail.email-change-ttl:15m}") Duration changeTtl,
            @Value("${app.mail.email-change-resend-cooldown:60s}") Duration resendCooldown
    ) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.localUserRepository = localUserRepository;
        this.oauthUserRepository = oauthUserRepository;
        this.emailChangeRequestRepository = emailChangeRequestRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationEmailComposer = verificationEmailComposer;
        this.changeTtl = changeTtl;
        this.resendCooldown = resendCooldown;
    }

    @Transactional
    public void appendProfileFlags(Map<String, Object> payload, UserEntity user) {
        Policy policy = policyFor(user);
        EmailChangeRequestEntity pending = activeRequest(user.getId()).orElse(null);
        payload.put("hasPassword", policy.hasPassword());
        payload.put("emailManagedBy", policy.emailManagedBy());
        payload.put("canEditEmail", pending == null && policy.canEditEmail());
        payload.put("pendingEmail", pending != null ? pending.getNewEmail() : null);
        payload.put("pendingExpiresAt", pending != null ? pending.getExpiresAt() : null);
    }

    @Transactional
    public Map<String, Object> start(StartEmailChangeRequestDto dto) {
        UserEntity user = authService.requireCurrentUser();
        Policy policy = policyFor(user);
        if (activeRequest(user.getId()).isPresent()) {
            throw new EmailChangeNotAllowedException(
                    "Finish or cancel the current email change before starting another."
            );
        }

        String newEmail = dto.email().trim().toLowerCase(Locale.ROOT);
        String currentEmail = blankToNull(user.getEmail());
        boolean adding = currentEmail == null;

        if (adding) {
            if (!policy.canAddEmail()) {
                throw new EmailChangeNotAllowedException("This email can't be changed from NumberLink.");
            }
        } else {
            if (!policy.canChangeEmail()) {
                if ("GOOGLE".equals(policy.emailManagedBy())) {
                    throw new EmailChangeNotAllowedException("This email is managed by Google.");
                }
                throw new EmailChangeNotAllowedException("This email can't be changed from NumberLink.");
            }
            if (currentEmail.equalsIgnoreCase(newEmail)) {
                throw new EmailChangeNotAllowedException("That's already your email address.");
            }
            verifyPassword(user, dto.password());
        }

        assertEmailAvailable(newEmail, user.getId());

        String code = randomCode();
        Instant now = Instant.now();
        Instant expiresAt = now.plus(changeTtl);

        emailChangeRequestRepository.deleteByUser_Id(user.getId());
        emailChangeRequestRepository.flush();

        EmailChangeRequestEntity request = new EmailChangeRequestEntity();
        request.setUser(user);
        request.setCurrentEmail(currentEmail);
        request.setNewEmail(newEmail);
        request.setConfirmCodeHash(hashCode(user.getId(), code));
        request.setAttempts(0);
        request.setExpiresAt(expiresAt);
        request.setLastSentAt(now);
        request.setCreatedAt(now);

        String cancelRaw = null;
        if (currentEmail != null) {
            VerificationTokenFactory.IssuedToken issued = VerificationTokenFactory.issue(user.getId());
            request.setCancelTokenHash(issued.tokenHash());
            cancelRaw = issued.rawToken();
        }

        emailChangeRequestRepository.save(request);
        sendCodeEmail(user, newEmail, code);
        if (currentEmail != null && cancelRaw != null) {
            sendCancelEmail(user, currentEmail, newEmail, cancelRaw);
        }

        return pendingPayload(newEmail, expiresAt);
    }

    @Transactional
    public Map<String, Object> resend() {
        UserEntity user = authService.requireCurrentUser();
        EmailChangeRequestEntity request = requireActive(user.getId());
        Instant now = Instant.now();
        if (request.getLastSentAt().plus(resendCooldown).isAfter(now)) {
            throw new EmailChangeResendTooSoonException();
        }

        String code = randomCode();
        request.setConfirmCodeHash(hashCode(user.getId(), code));
        request.setAttempts(0);
        request.setExpiresAt(now.plus(changeTtl));
        request.setLastSentAt(now);
        emailChangeRequestRepository.save(request);
        sendCodeEmail(user, request.getNewEmail(), code);
        return pendingPayload(request.getNewEmail(), request.getExpiresAt());
    }

    @Transactional
    public Map<String, Object> confirm(ConfirmEmailChangeRequestDto dto, HttpServletRequest httpRequest) {
        UserEntity user = authService.requireCurrentUser();
        EmailChangeRequestEntity request = requireActive(user.getId());

        String expected = request.getConfirmCodeHash();
        String actual = hashCode(user.getId(), dto.code().trim());
        if (!expected.equals(actual)) {
            int attempts = request.getAttempts() + 1;
            request.setAttempts(attempts);
            if (attempts >= MAX_ATTEMPTS) {
                emailChangeRequestRepository.delete(request);
                throw new InvalidEmailChangeCodeException(0);
            }
            emailChangeRequestRepository.save(request);
            throw new InvalidEmailChangeCodeException(MAX_ATTEMPTS - attempts);
        }

        assertEmailAvailable(request.getNewEmail(), user.getId());

        Instant now = Instant.now();
        user.setEmail(request.getNewEmail());
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(now);
        userRepository.save(user);
        emailChangeRequestRepository.delete(request);
        authService.bumpSessionEpoch(user, httpRequest);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", user.getEmail());
        payload.put("emailVerified", true);
        appendProfileFlags(payload, user);
        return payload;
    }

    @Transactional
    public void cancelFromSettings() {
        UserEntity user = authService.requireCurrentUser();
        EmailChangeRequestEntity request = emailChangeRequestRepository.findByUser_Id(user.getId())
                .orElseThrow(NoPendingEmailChangeException::new);
        emailChangeRequestRepository.delete(request);
    }

    @Transactional
    public boolean cancelFromToken(String rawToken) {
        if (!StringUtils.hasText(rawToken)) {
            return false;
        }
        String tokenHash = VerificationTokenFactory.hash(rawToken.trim());
        Optional<EmailChangeRequestEntity> found = emailChangeRequestRepository.findByCancelTokenHash(tokenHash);
        if (found.isEmpty()) {
            return false;
        }
        EmailChangeRequestEntity request = found.get();
        if (request.getExpiresAt().isBefore(Instant.now())) {
            emailChangeRequestRepository.delete(request);
            return false;
        }
        UserEntity user = request.getUser();
        emailChangeRequestRepository.delete(request);
        authService.bumpSessionEpoch(user, null);
        return true;
    }

    private void verifyPassword(UserEntity user, String password) {
        if (!StringUtils.hasText(password)) {
            throw new EmailChangeNotAllowedException("Confirm your password to change email.");
        }
        LocalUserEntity local = localUserRepository.findByUserId(user.getId())
                .orElseThrow(WrongPasswordException::new);
        if (!passwordEncoder.matches(password, local.getEncodedPassword())) {
            throw new WrongPasswordException();
        }
    }

    private void assertEmailAvailable(String newEmail, UUID userId) {
        Optional<UserEntity> taken = userRepository.findByEmailIgnoreCase(newEmail);
        if (taken.isPresent() && !taken.get().getId().equals(userId)) {
            throw new EmailTakenException();
        }
        if (emailChangeRequestRepository.existsByNewEmailIgnoreCaseAndExpiresAtAfterAndUser_IdNot(
                newEmail,
                Instant.now(),
                userId
        )) {
            throw new EmailTakenException();
        }
    }

    private EmailChangeRequestEntity requireActive(UUID userId) {
        return activeRequest(userId).orElseThrow(NoPendingEmailChangeException::new);
    }

    private Optional<EmailChangeRequestEntity> activeRequest(UUID userId) {
        Optional<EmailChangeRequestEntity> found = emailChangeRequestRepository.findByUser_Id(userId);
        if (found.isEmpty()) {
            return Optional.empty();
        }
        EmailChangeRequestEntity request = found.get();
        if (request.getExpiresAt().isBefore(Instant.now())) {
            emailChangeRequestRepository.delete(request);
            return Optional.empty();
        }
        return Optional.of(request);
    }

    private Policy policyFor(UserEntity user) {
        boolean hasPassword = localUserRepository.findByUserId(user.getId()).isPresent();
        boolean hasGoogle = oauthUserRepository.existsByUser_IdAndProvider(user.getId(), OauthProvider.GOOGLE);
        boolean hasEmail = StringUtils.hasText(user.getEmail());
        String managedBy = hasGoogle && !hasPassword ? "GOOGLE" : null;
        boolean canAddEmail = !hasEmail;
        boolean canChangeEmail = hasEmail && hasPassword;
        boolean canEditEmail = canAddEmail || canChangeEmail;
        return new Policy(hasPassword, managedBy, canAddEmail, canChangeEmail, canEditEmail);
    }

    private void sendCodeEmail(UserEntity user, String to, String code) {
        int minutes = (int) Math.max(1, changeTtl.toMinutes());
        String html = verificationEmailComposer.renderEmailChangeCodeHtml(user.getUsername(), code, minutes);
        try {
            emailService.sendHtml(to, "Your NumberLink confirmation code", html);
        } catch (EmailSendException ex) {
            throw new EmailSendException("Failed to send confirmation code", ex);
        }
    }

    private void sendCancelEmail(UserEntity user, String to, String newEmail, String rawToken) {
        int minutes = (int) Math.max(1, changeTtl.toMinutes());
        String cancelUrl = verificationEmailComposer.buildEmailChangeCancelLink(rawToken);
        String html = verificationEmailComposer.renderEmailChangeCancelHtml(
                user.getUsername(),
                newEmail,
                cancelUrl,
                minutes
        );
        try {
            emailService.sendHtml(to, "Your NumberLink email is being changed", html);
        } catch (EmailSendException ex) {
            throw new EmailSendException("Failed to send email-change notice", ex);
        }
    }

    private static Map<String, Object> pendingPayload(String pendingEmail, Instant expiresAt) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("pendingEmail", pendingEmail);
        payload.put("pendingExpiresAt", expiresAt);
        return payload;
    }

    private static String randomCode() {
        return String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
    }

    private static String hashCode(UUID userId, String code) {
        return VerificationTokenFactory.hash(userId + ":" + code);
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private record Policy(
            boolean hasPassword,
            String emailManagedBy,
            boolean canAddEmail,
            boolean canChangeEmail,
            boolean canEditEmail
    ) {}
}
