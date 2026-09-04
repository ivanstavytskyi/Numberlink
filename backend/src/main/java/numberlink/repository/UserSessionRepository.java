package numberlink.repository;

import numberlink.entity.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSessionEntity, UUID> {

    Optional<UserSessionEntity> findByHttpSessionHash(String httpSessionHash);

    Optional<UserSessionEntity> findByIdAndUser_IdAndRevokedAtIsNull(UUID id, UUID userId);

    List<UserSessionEntity> findByUser_IdAndRevokedAtIsNullOrderByLastSeenAtDesc(UUID userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE UserSession s
            SET s.revokedAt = :now
            WHERE s.user.id = :userId
              AND s.revokedAt IS NULL
              AND s.id <> :keepId
            """)
    int revokeOthers(
            @Param("userId") UUID userId,
            @Param("keepId") UUID keepId,
            @Param("now") Instant now
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE UserSession s
            SET s.revokedAt = :now
            WHERE s.user.id = :userId
              AND s.revokedAt IS NULL
            """)
    int revokeAll(@Param("userId") UUID userId, @Param("now") Instant now);
}
