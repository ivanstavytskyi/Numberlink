package numberlink.service.user;

import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UsernameGenerator {

    private final Faker faker = new Faker(Locale.of("en"));

    public String generate() {
        for (int attempt = 0; attempt < 12; attempt++) {
            String candidate = sanitize(faker.internet().username());
            if (candidate.length() >= 3) {
                return candidate;
            }
        }
        return "player" + ThreadLocalRandom.current().nextInt(1000, 10_000);
    }

    private static String sanitize(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        String cleaned = raw.trim()
                .replaceAll("[^a-zA-Z0-9_]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
        if (cleaned.length() > 32) {
            cleaned = cleaned.substring(0, 32).replaceAll("_$", "");
        }
        return cleaned;
    }
}
