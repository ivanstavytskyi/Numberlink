package numberlink.service.auth;

final class UserAgentParser {

    record Summary(String device, String os, String browser) {}

    private UserAgentParser() {}

    static Summary parse(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return new Summary("Unknown device", "Unknown", "Unknown");
        }
        String ua = userAgent.trim();
        String os = detectOs(ua);
        String browser = detectBrowser(ua);
        return new Summary(clip(detectDevice(ua, os), 128), clip(os, 64), clip(browser, 64));
    }

    private static String detectOs(String ua) {
        if (ua.contains("iPhone") || ua.contains("iPad") || ua.contains("iPod") || ua.contains("CPU OS ")) {
            return "iOS";
        }
        if (ua.contains("Android")) {
            return "Android";
        }
        if (ua.contains("Windows")) {
            return "Windows";
        }
        if (ua.contains("Mac OS X") || ua.contains("Macintosh")) {
            return "macOS";
        }
        if (ua.contains("CrOS")) {
            return "Chrome OS";
        }
        if (ua.contains("Linux")) {
            return "Linux";
        }
        return "Unknown";
    }

    private static String detectBrowser(String ua) {
        if (ua.contains("Edg/") || ua.contains("EdgA/") || ua.contains("EdgiOS/")) {
            return "Edge";
        }
        if (ua.contains("OPR/") || ua.contains("Opera")) {
            return "Opera";
        }
        if (ua.contains("SamsungBrowser/")) {
            return "Samsung Internet";
        }
        if (ua.contains("Firefox/") || ua.contains("FxiOS/")) {
            return "Firefox";
        }
        if (ua.contains("Chrome/") || ua.contains("CriOS/")) {
            return "Chrome";
        }
        if (ua.contains("Safari/") && !ua.contains("Chrome") && !ua.contains("CriOS")) {
            return "Safari";
        }
        return "Unknown";
    }

    private static String detectDevice(String ua, String os) {
        if (ua.contains("iPhone") || ua.contains("iPod")) {
            return "iPhone";
        }
        if (ua.contains("iPad")) {
            return "iPad";
        }
        if (ua.contains("Android")) {
            return ua.contains("Mobile") ? "Android phone" : "Android tablet";
        }
        return switch (os) {
            case "Windows" -> "Windows PC";
            case "macOS" -> "Mac";
            case "Linux" -> "Linux PC";
            case "Chrome OS" -> "Chromebook";
            default -> "Unknown device";
        };
    }

    private static String clip(String value, int max) {
        if (value.length() <= max) {
            return value;
        }
        return value.substring(0, max);
    }
}
