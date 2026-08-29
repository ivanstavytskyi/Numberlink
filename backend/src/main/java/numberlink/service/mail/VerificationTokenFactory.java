package numberlink.service.mail;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

/**
 * Issues opaque email-verification tokens.
 * <p>
 * Raw token (URL): Base64URL(userId bytes ‖ random 32 bytes).<br>
 * Stored value: SHA-256 hex of the raw token (64 chars).
 */
public final class VerificationTokenFactory {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int RANDOM_BYTES = 32;

    private VerificationTokenFactory() {
    }

    public record IssuedToken(String rawToken, String tokenHash) {
    }

    public static IssuedToken issue(UUID userId) {
        byte[] random = new byte[RANDOM_BYTES];
        SECURE_RANDOM.nextBytes(random);

        byte[] userBytes = uuidBytes(userId);
        byte[] material = new byte[userBytes.length + random.length];
        System.arraycopy(userBytes, 0, material, 0, userBytes.length);
        System.arraycopy(random, 0, material, userBytes.length, random.length);

        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(material);
        return new IssuedToken(rawToken, hash(rawToken));
    }

    public static String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }

    private static byte[] uuidBytes(UUID id) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(id.getMostSignificantBits());
        buffer.putLong(id.getLeastSignificantBits());
        return buffer.array();
    }
}
