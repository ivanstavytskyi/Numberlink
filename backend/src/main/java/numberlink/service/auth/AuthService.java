package numberlink.service.auth;

import numberlink.dto.user.login.request.LoginRequestDto;
import numberlink.dto.user.register.request.RegisterRequestDto;
import numberlink.entity.LocalUserEntity;
import numberlink.entity.MailTokenEntity;
import numberlink.entity.UserEntity;
import numberlink.entity.enums.MailTokenAction;
import numberlink.exceptions.AccountHasNoEmailException;
import numberlink.exceptions.AccountNotFoundException;
import numberlink.exceptions.EmailAlreadyVerifiedException;
import numberlink.exceptions.EmailNotVerifiedException;
import numberlink.exceptions.EmailSendException;
import numberlink.exceptions.InvalidCredentialsException;
import numberlink.exceptions.InvalidPasswordResetTokenException;
import numberlink.exceptions.InvalidVerificationTokenException;
import numberlink.exceptions.NoLocalPasswordException;
import numberlink.exceptions.NotAuthenticatedException;
import numberlink.exceptions.PasswordUnchangedException;
import numberlink.exceptions.WrongPasswordException;
import numberlink.exceptions.TotpChallengeExpiredException;
import numberlink.exceptions.UsernameOrEmailTakenException;
import numberlink.exceptions.UsernameSuggestFailedException;
import numberlink.repository.LocalUserRepository;
import numberlink.repository.MailTokenRepository;
import numberlink.repository.UserRepository;
import numberlink.service.user.UsernameGenerator;
import numberlink.service.mail.EmailService;
import numberlink.service.mail.VerificationEmailComposer;
import numberlink.service.mail.VerificationTokenFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthService {
    public static final String SESSION_EPOCH_ATTR = "nl.sessionEpoch";
    public static final String PENDING_2FA_USER_ID = "nl.pending2faUserId";
    public static final String PENDING_2FA_EXPIRES = "nl.pending2faExpires";
    public static final String PENDING_2FA_ATTEMPTS = "nl.pending2faAttempts";

    private static final Duration TWO_FACTOR_CHALLENGE_TTL = Duration.ofMinutes(5);
    private static final int TWO_FACTOR_MAX_ATTEMPTS = 8;

    private final UserRepository userRepository;
    private final LocalUserRepository localUserRepository;
    private final MailTokenRepository mailTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsernameGenerator usernameGenerator;
    private final EmailService emailService;
    private final VerificationEmailComposer verificationEmailComposer;
    private final TotpService totpService;
    private final UserSessionService userSessionService;
    private final Duration verifyTokenTtl;
    private final Duration resetTokenTtl;

    public AuthService(UserRepository userRepository,
                       LocalUserRepository localUserRepository,
                       MailTokenRepository mailTokenRepository,
                       PasswordEncoder passwordEncoder,
                       UsernameGenerator usernameGenerator,
                       EmailService emailService,
                       VerificationEmailComposer verificationEmailComposer,
                       TotpService totpService,
                       UserSessionService userSessionService,
                       @Value("${app.mail.verify-token-ttl:48h}") Duration verifyTokenTtl,
                       @Value("${app.mail.reset-token-ttl:2h}") Duration resetTokenTtl) {
        this.userRepository = userRepository;
        this.localUserRepository = localUserRepository;
        this.mailTokenRepository = mailTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.usernameGenerator = usernameGenerator;
        this.emailService = emailService;
        this.verificationEmailComposer = verificationEmailComposer;
        this.totpService = totpService;
        this.userSessionService = userSessionService;
        this.verifyTokenTtl = verifyTokenTtl;
        this.resetTokenTtl = resetTokenTtl;
    }

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public UserEntity register(RegisterRequestDto dto, HttpServletRequest request, HttpServletResponse response) {
        if (userRepository.existsByEmailIgnoreCase(dto.email())
                || userRepository.existsByUsernameIgnoreCase(dto.username())) {
            throw new UsernameOrEmailTakenException();
        }
        UserEntity user = new UserEntity();
        user.setUsername(dto.username().trim());
        user.setEmail(dto.email().trim().toLowerCase());
        user.setEmailVerified(false);
        user.setCreatedAt(Instant.now());
        user = userRepository.saveAndFlush(user);

        LocalUserEntity local = new LocalUserEntity();
        local.setUser(user);
        local.setEncodedPassword(passwordEncoder.encode(dto.password()));
        localUserRepository.save(local);

        // No session until email is verified — frontend shows the verify modal.
        sendSignupConfirmationEmail(user);
        return user;
    }

    public void resendVerificationEmail(String email) {
        String normalized = email.trim().toLowerCase();
        UserEntity user = userRepository.findByEmailIgnoreCase(normalized)
                .orElseThrow(AccountNotFoundException::new);

        if (user.isEmailVerified()) {
            throw new EmailAlreadyVerifiedException();
        }

        sendSignupConfirmationEmail(user);
    }

    public UserEntity verifyEmail(String rawToken, HttpServletRequest request, HttpServletResponse response) {
        if (!StringUtils.hasText(rawToken)) {
            throw new InvalidVerificationTokenException();
        }

        String tokenHash = VerificationTokenFactory.hash(rawToken.trim());
        MailTokenEntity mail = mailTokenRepository
                .findByTokenHashAndActionWithUser(tokenHash, MailTokenAction.EMAIL_VERIFY)
                .orElseThrow(InvalidVerificationTokenException::new);

        if (mail.getUsedAt() != null) {
            throw new InvalidVerificationTokenException();
        }
        if (mail.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidVerificationTokenException();
        }

        UserEntity user = mail.getUser();
        Instant now = Instant.now();
        mail.setUsedAt(now);

        if (!user.isEmailVerified()) {
            user.setEmailVerified(true);
            user.setEmailVerifiedAt(now);
            userRepository.save(user);
        }

        mailTokenRepository.save(mail);
        establishSession(user, request, response);
        return user;
    }

    private void sendSignupConfirmationEmail(UserEntity user) {
        String to = user.getEmail();
        if (to == null || to.isBlank()) {
            throw new AccountHasNoEmailException();
        }

        mailTokenRepository.deleteUnusedByUserIdAndAction(user.getId(), MailTokenAction.EMAIL_VERIFY);

        VerificationTokenFactory.IssuedToken issued = VerificationTokenFactory.issue(user.getId());
        Instant now = Instant.now();

        MailTokenEntity mail = new MailTokenEntity();
        mail.setUser(user);
        mail.setAction(MailTokenAction.EMAIL_VERIFY);
        mail.setTokenHash(issued.tokenHash());
        mail.setExpiresAt(now.plus(verifyTokenTtl));
        mail.setCreatedAt(now);
        mailTokenRepository.save(mail);

        String subject = "Confirm your NumberLink account";
        String verifyUrl = verificationEmailComposer.buildVerifyLink(issued.rawToken());
        String html = verificationEmailComposer.renderConfirmHtml(user.getUsername(), verifyUrl);

        try {
            emailService.sendHtml(to, subject, html);
        } catch (EmailSendException ex) {
            throw new EmailSendException("Failed to send confirmation email", ex);
        }
    }

    /**
     * Always succeeds from the caller's perspective (no email enumeration).
     * Sends a reset mail only when a local-password account with that email exists.
     */
    public void requestPasswordReset(String email) {
        String normalized = email.trim().toLowerCase();
        Optional<UserEntity> userOpt = userRepository.findByEmailIgnoreCase(normalized);
        if (userOpt.isEmpty()) {
            return;
        }

        UserEntity user = userOpt.get();
        if (localUserRepository.findByUserId(user.getId()).isEmpty()) {
            return;
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }

        sendPasswordResetEmail(user);
    }

    /**
     * Consumes a password-reset token and updates the local password.
     * Does not establish a session — the user must log in afterward.
     */
    public void resetPassword(String rawToken, String newPassword) {
        if (!StringUtils.hasText(rawToken)) {
            throw new InvalidPasswordResetTokenException();
        }

        String tokenHash = VerificationTokenFactory.hash(rawToken.trim());
        MailTokenEntity mail = mailTokenRepository
                .findByTokenHashAndActionWithUser(tokenHash, MailTokenAction.PASSWORD_RESET)
                .orElseThrow(InvalidPasswordResetTokenException::new);

        if (mail.getUsedAt() != null || mail.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidPasswordResetTokenException();
        }

        UserEntity user = mail.getUser();
        if (localUserRepository.findByUserId(user.getId()).isEmpty()) {
            throw new InvalidPasswordResetTokenException();
        }

        Instant now = Instant.now();
        mail.setUsedAt(now);
        mailTokenRepository.save(mail);
        mailTokenRepository.deleteUnusedByUserIdAndAction(user.getId(), MailTokenAction.PASSWORD_RESET);
        localUserRepository.updateEncodedPassword(user.getId(), passwordEncoder.encode(newPassword));
    }

    private void sendPasswordResetEmail(UserEntity user) {
        String to = user.getEmail();
        if (to == null || to.isBlank()) {
            throw new AccountHasNoEmailException();
        }

        mailTokenRepository.deleteUnusedByUserIdAndAction(user.getId(), MailTokenAction.PASSWORD_RESET);

        VerificationTokenFactory.IssuedToken issued = VerificationTokenFactory.issue(user.getId());
        Instant now = Instant.now();

        MailTokenEntity mail = new MailTokenEntity();
        mail.setUser(user);
        mail.setAction(MailTokenAction.PASSWORD_RESET);
        mail.setTokenHash(issued.tokenHash());
        mail.setExpiresAt(now.plus(resetTokenTtl));
        mail.setCreatedAt(now);
        mailTokenRepository.save(mail);

        String subject = "Reset your NumberLink password";
        String resetUrl = verificationEmailComposer.buildPasswordResetLink(issued.rawToken());
        String html = verificationEmailComposer.renderResetPasswordHtml(
                user.getUsername(),
                resetUrl,
                resetTokenTtl
        );

        try {
            emailService.sendHtml(to, subject, html);
        } catch (EmailSendException ex) {
            throw new EmailSendException("Failed to send password reset email", ex);
        }
    }

    public LoginOutcome login(LoginRequestDto dto, HttpServletRequest request, HttpServletResponse response) {
        String login = dto.login().trim();
        UserEntity user = resolveUser(login)
                .orElseThrow(InvalidCredentialsException::new);

        LocalUserEntity local = localUserRepository.findByUserId(user.getId())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(dto.password(), local.getEncodedPassword())) {
            throw new InvalidCredentialsException();
        }

        if (!user.isEmailVerified()) {
            String email = user.getEmail() != null
                    ? user.getEmail()
                    : (login.contains("@") ? login.toLowerCase() : "");
            throw new EmailNotVerifiedException(email);
        }

        if (totpService.isEnabled(user.getId())) {
            beginTwoFactorChallenge(user, request);
            return new LoginOutcome(null, true);
        }

        establishSession(user, request, response);
        return new LoginOutcome(user, false);
    }

    public void clearAuthentication(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        securityContextRepository.saveContext(
                SecurityContextHolder.createEmptyContext(),
                request,
                response
        );
    }

    public void beginTwoFactorChallenge(UserEntity user, HttpServletRequest request) {
        var session = request.getSession(true);
        session.setAttribute(PENDING_2FA_USER_ID, user.getId());
        session.setAttribute(PENDING_2FA_EXPIRES, Instant.now().plus(TWO_FACTOR_CHALLENGE_TTL));
        session.setAttribute(PENDING_2FA_ATTEMPTS, 0);
    }

    public UserEntity completeTwoFactorLogin(String code, HttpServletRequest request, HttpServletResponse response) {
        var session = request.getSession(false);
        if (session == null) {
            throw new TotpChallengeExpiredException();
        }
        Object rawId = session.getAttribute(PENDING_2FA_USER_ID);
        Object rawExpires = session.getAttribute(PENDING_2FA_EXPIRES);
        if (!(rawId instanceof UUID userId) || !(rawExpires instanceof Instant expires)) {
            throw new TotpChallengeExpiredException();
        }
        if (Instant.now().isAfter(expires)) {
            clearTwoFactorChallenge(session);
            throw new TotpChallengeExpiredException();
        }

        int attempts = session.getAttribute(PENDING_2FA_ATTEMPTS) instanceof Integer n ? n : 0;
        if (attempts >= TWO_FACTOR_MAX_ATTEMPTS) {
            clearTwoFactorChallenge(session);
            throw new TotpChallengeExpiredException();
        }
        session.setAttribute(PENDING_2FA_ATTEMPTS, attempts + 1);

        totpService.verifyEnabledCode(userId, code);

        UserEntity user = userRepository.findById(userId).orElseThrow(TotpChallengeExpiredException::new);
        clearTwoFactorChallenge(session);
        establishSession(user, request, response);
        return user;
    }

    private static void clearTwoFactorChallenge(jakarta.servlet.http.HttpSession session) {
        session.removeAttribute(PENDING_2FA_USER_ID);
        session.removeAttribute(PENDING_2FA_EXPIRES);
        session.removeAttribute(PENDING_2FA_ATTEMPTS);
    }

    public record LoginOutcome(UserEntity user, boolean twoFactorRequired) {}

    public void establishSession(UserEntity user, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                user.getId().toString(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        request.getSession(true).setAttribute(SESSION_EPOCH_ATTR, user.getSessionEpoch());
        userSessionService.remember(user.getId(), request);
    }

    public void bumpSessionEpoch(UserEntity user, HttpServletRequest request) {
        userRepository.incrementSessionEpoch(user.getId());
        int next = userRepository.findSessionEpochById(user.getId()).orElse(user.getSessionEpoch() + 1);
        user.setSessionEpoch(next);
        if (request != null) {
            var session = request.getSession(false);
            if (session != null) {
                session.setAttribute(SESSION_EPOCH_ATTR, next);
            }
            userSessionService.revokeOthers(user.getId(), request);
        } else {
            userSessionService.revokeAll(user.getId());
        }
    }

    private Optional<UserEntity> resolveUser(String login) {
        if (login.contains("@")) {
            return userRepository.findByEmailIgnoreCase(login);
        }
        return userRepository.findByUsernameIgnoreCase(login);
    }

    @Transactional(readOnly = true)
    public UserEntity requireCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new NotAuthenticatedException();
        }

        UUID userId;
        try {
            userId = UUID.fromString(authentication.getName());
        } catch (IllegalArgumentException ex) {
            throw new NotAuthenticatedException();
        }

        return userRepository.findById(userId)
                .orElseThrow(NotAuthenticatedException::new);
    }

    public void changePassword(String currentPassword, String newPassword, HttpServletRequest request) {
        UserEntity user = requireCurrentUser();
        LocalUserEntity local = localUserRepository.findByUserId(user.getId())
                .orElseThrow(NoLocalPasswordException::new);

        if (!passwordEncoder.matches(currentPassword, local.getEncodedPassword())) {
            throw new WrongPasswordException();
        }
        if (passwordEncoder.matches(newPassword, local.getEncodedPassword())) {
            throw new PasswordUnchangedException();
        }

        int updated = localUserRepository.updateEncodedPassword(
                user.getId(),
                passwordEncoder.encode(newPassword)
        );
        if (updated != 1) {
            throw new NoLocalPasswordException();
        }
        bumpSessionEpoch(user, request);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        userSessionService.revokeCurrent(request);
        SecurityContextHolder.clearContext();
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        securityContextRepository.saveContext(
                SecurityContextHolder.createEmptyContext(),
                request,
                response
        );
    }

    /** Unique display name for the signup form (letters, digits, underscore). */
    @Transactional(readOnly = true)
    public String suggestUsername() {
        for (int i = 0; i < 32; i++) {
            String candidate = usernameGenerator.generate();
            if (candidate != null
                    && candidate.length() >= 3
                    && !userRepository.existsByUsernameIgnoreCase(candidate)) {
                return candidate;
            }
        }
        throw new UsernameSuggestFailedException();
    }
}
