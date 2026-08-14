package numberlink.dto.user.session.response;

import java.time.Instant;
import java.util.UUID;

public record UserSessionResponseDto(
        UUID id,
        String device,
        String os,
        String browser,
        Instant lastSeenAt,
        Instant createdAt,
        boolean current
) {}
