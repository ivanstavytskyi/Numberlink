package numberlink.service.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAgentParserTest {

    @Test
    void parse_whenBlank_returnsUnknown() {
        assertEquals(new UserAgentParser.Summary("Unknown device", "Unknown", "Unknown"),
                UserAgentParser.parse(null));
        assertEquals(new UserAgentParser.Summary("Unknown device", "Unknown", "Unknown"),
                UserAgentParser.parse(""));
    }

    @Test
    void parse_whenWindowsChrome_detectsPcAndChrome() {
        String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
        assertEquals(new UserAgentParser.Summary("Windows PC", "Windows", "Chrome"),
                UserAgentParser.parse(ua));
    }

    @Test
    void parse_whenIphoneSafari_detectsIos() {
        String ua = "Mozilla/5.0 (iPhone; CPU iPhone OS 18_7 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/26.4 Mobile/15E148 Safari/604.1 OPX/3.3.0";
        assertEquals(new UserAgentParser.Summary("iPhone", "iOS", "Safari"),
                UserAgentParser.parse(ua));
    }

    @Test
    void parse_whenAndroidFirefox_detectsPhone() {
        String ua = "Mozilla/5.0 (Android 13; Mobile; rv:128.0) Gecko/128.0 Firefox/128.0";
        assertEquals(new UserAgentParser.Summary("Android phone", "Android", "Firefox"),
                UserAgentParser.parse(ua));
    }

    @Test
    void parse_whenEdge_detectsEdgeNotChrome() {
        String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 Edg/120.0.0.0";
        assertEquals(new UserAgentParser.Summary("Windows PC", "Windows", "Edge"),
                UserAgentParser.parse(ua));
    }
}
