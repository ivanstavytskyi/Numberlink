package numberlink.service.auth;

import numberlink.entity.UserEntity;
import numberlink.entity.UserTotpEntity;
import numberlink.exceptions.InvalidTotpCodeException;
import numberlink.exceptions.TotpAlreadyEnabledException;
import numberlink.exceptions.TotpNotEnabledException;
import numberlink.exceptions.TotpSetupRequiredException;
import numberlink.repository.UserTotpRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class TotpService {

    private static final String ISSUER = "NumberLink";

    private final UserTotpRepository userTotpRepository;

    public TotpService(UserTotpRepository userTotpRepository) {
        this.userTotpRepository = userTotpRepository;
    }

    @Transactional(readOnly = true)
    public boolean isEnabled(UUID userId) {
        return userTotpRepository.findById(userId)
                .map(UserTotpEntity::isEnabled)
                .orElse(false);
    }

    public void appendFlag(Map<String, Object> payload, UserEntity user) {
        payload.put("twoFactorEnabled", isEnabled(user.getId()));
    }

    public Map<String, Object> startSetup(UserEntity user) {
        UserTotpEntity row = userTotpRepository.findById(user.getId()).orElseGet(() -> {
            UserTotpEntity created = new UserTotpEntity();
            created.setUser(user);
            created.setEnabled(false);
            return created;
        });
        if (row.isEnabled()) {
            throw new TotpAlreadyEnabledException();
        }

        String secret = TotpCodes.generateSecret();
        row.setPendingSecret(secret);
        userTotpRepository.save(row);

        String account = user.getEmail() != null && !user.getEmail().isBlank()
                ? user.getEmail()
                : user.getUsername();
        String otpauth = TotpQr.otpauthUrl(ISSUER, account, secret);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("secret", TotpCodes.formatSecret(secret));
        body.put("otpauthUrl", otpauth);
        body.put("qrDataUrl", TotpQr.pngDataUrl(otpauth));
        return body;
    }

    public void cancelSetup(UserEntity user) {
        userTotpRepository.findById(user.getId()).ifPresent(row -> {
            if (row.isEnabled()) {
                row.setPendingSecret(null);
                userTotpRepository.save(row);
                return;
            }
            userTotpRepository.delete(row);
        });
    }

    public void confirmSetup(UserEntity user, String code) {
        UserTotpEntity row = userTotpRepository.findById(user.getId())
                .orElseThrow(TotpSetupRequiredException::new);
        if (row.isEnabled()) {
            throw new TotpAlreadyEnabledException();
        }
        String pending = row.getPendingSecret();
        if (pending == null || pending.isBlank()) {
            throw new TotpSetupRequiredException();
        }
        if (!TotpCodes.verify(pending, code)) {
            throw new InvalidTotpCodeException();
        }
        row.setSecret(pending);
        row.setPendingSecret(null);
        row.setEnabled(true);
        row.setConfirmedAt(Instant.now());
        userTotpRepository.save(row);
    }

    public void disable(UserEntity user, String code) {
        UserTotpEntity row = userTotpRepository.findById(user.getId())
                .orElseThrow(TotpNotEnabledException::new);
        if (!row.isEnabled() || row.getSecret() == null) {
            throw new TotpNotEnabledException();
        }
        if (!TotpCodes.verify(row.getSecret(), code)) {
            throw new InvalidTotpCodeException();
        }
        userTotpRepository.delete(row);
    }

    public void verifyEnabledCode(UUID userId, String code) {
        UserTotpEntity row = userTotpRepository.findById(userId)
                .orElseThrow(TotpNotEnabledException::new);
        if (!row.isEnabled() || row.getSecret() == null) {
            throw new TotpNotEnabledException();
        }
        if (!TotpCodes.verify(row.getSecret(), code)) {
            throw new InvalidTotpCodeException();
        }
    }
}
