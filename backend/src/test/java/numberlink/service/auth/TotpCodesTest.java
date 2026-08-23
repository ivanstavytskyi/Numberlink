package numberlink.service.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TotpCodesTest {

    @Test
    void verify_whenCodeIsNotSixDigits_returnsFalse() {
        String secret = TotpCodes.generateSecret();

        assertFalse(TotpCodes.verify(secret, null));
        assertFalse(TotpCodes.verify(secret, "12"));
        assertFalse(TotpCodes.verify(secret, "12345"));
        assertFalse(TotpCodes.verify(secret, "1234567"));
        assertFalse(TotpCodes.verify(secret, "abcdef"));
    }

    @Test
    void verify_whenSecretBlank_returnsFalse() {
        String secret = null;
        assertFalse(TotpCodes.verify(secret, "123456"));
        assertFalse(TotpCodes.verify("", "123456"));
        assertFalse(TotpCodes.verify("   ", "123456"));
    }

    @Test
    void verify_whenCodeWrong_returnsFalse() {
        String secret = TotpCodes.generateSecret();

        assertFalse(TotpCodes.verify(secret, "000000"));
    }

    @Test
    void normalizeSecret_stripsSpacesAndUppercases() {
        assertEquals("SECRET", TotpCodes.normalizeSecret("secret "));
        assertEquals("", TotpCodes.normalizeSecret(""));
        assertEquals("1ABCDEF", TotpCodes.normalizeSecret("1ABcdef "));
        assertEquals("", TotpCodes.normalizeSecret(null));
    }

    @Test
    void formatSecret_groupsByFour() {
        assertEquals("ABCD EFGH", TotpCodes.formatSecret("ABCDEFGH"));
    }

    @Test
    void generateSecret_returnsBase32() {
        String secret = TotpCodes.generateSecret();

        assertFalse(secret.isBlank());
        assertEquals(32, secret.length());
        assertTrue(secret.matches("[A-Z2-7]+"));
    }
}
