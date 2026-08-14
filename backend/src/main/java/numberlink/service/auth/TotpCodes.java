package numberlink.service.auth;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Locale;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

final class TotpCodes {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int PERIOD_SECONDS = 30;
    private static final int WINDOW = 1;

    private TotpCodes() {
    }

    static String generateSecret() {
        byte[] bytes = new byte[20];
        RANDOM.nextBytes(bytes);
        return base32Encode(bytes);
    }

    static boolean verify(String secret, String code) {
        String normalized = normalizeCode(code);
        if (normalized == null || secret == null || secret.isBlank()) {
            return false;
        }
        long counter = Instant.now().getEpochSecond() / PERIOD_SECONDS;
        for (int i = -WINDOW; i <= WINDOW; i++) {
            if (constantTimeEquals(hotp(secret, counter + i), normalized)) {
                return true;
            }
        }
        return false;
    }

    static String normalizeSecret(String secret) {
        if (secret == null) {
            return "";
        }
        return secret.replace(" ", "").toUpperCase(Locale.ROOT);
    }

    static String formatSecret(String secret) {
        String compact = normalizeSecret(secret);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < compact.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                out.append(' ');
            }
            out.append(compact.charAt(i));
        }
        return out.toString();
    }

    private static String normalizeCode(String code) {
        if (code == null) {
            return null;
        }
        String digits = code.replaceAll("\\D", "");
        return digits.length() == 6 ? digits : null;
    }

    private static String hotp(String secret, long counter) {
        byte[] key = base32Decode(normalizeSecret(secret));
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(counter);
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hash = mac.doFinal(buffer.array());
            int offset = hash[hash.length - 1] & 0x0f;
            int binary = ((hash[offset] & 0x7f) << 24)
                    | ((hash[offset + 1] & 0xff) << 16)
                    | ((hash[offset + 2] & 0xff) << 8)
                    | (hash[offset + 3] & 0xff);
            int otp = binary % 1_000_000;
            return String.format("%06d", otp);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not generate TOTP code", ex);
        }
    }

    private static String base32Encode(byte[] data) {
        StringBuilder out = new StringBuilder((data.length * 8 + 4) / 5);
        int buffer = 0;
        int bitsLeft = 0;
        for (byte b : data) {
            buffer = (buffer << 8) | (b & 0xff);
            bitsLeft += 8;
            while (bitsLeft >= 5) {
                out.append(ALPHABET.charAt((buffer >> (bitsLeft - 5)) & 31));
                bitsLeft -= 5;
            }
        }
        if (bitsLeft > 0) {
            out.append(ALPHABET.charAt((buffer << (5 - bitsLeft)) & 31));
        }
        return out.toString();
    }

    private static byte[] base32Decode(String encoded) {
        int buffer = 0;
        int bitsLeft = 0;
        ByteBuffer out = ByteBuffer.allocate(encoded.length() * 5 / 8 + 1);
        for (int i = 0; i < encoded.length(); i++) {
            int val = ALPHABET.indexOf(encoded.charAt(i));
            if (val < 0) {
                continue;
            }
            buffer = (buffer << 5) | val;
            bitsLeft += 5;
            if (bitsLeft >= 8) {
                out.put((byte) ((buffer >> (bitsLeft - 8)) & 0xff));
                bitsLeft -= 8;
            }
        }
        byte[] bytes = new byte[out.position()];
        out.rewind();
        out.get(bytes);
        return bytes;
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
