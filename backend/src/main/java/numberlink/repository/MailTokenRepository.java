package numberlink.repository;

import numberlink.entity.MailTokenEntity;
import numberlink.entity.enums.MailTokenAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MailTokenRepository extends JpaRepository<MailTokenEntity, Long> {

    @Query("""
            select m from MailToken m
            join fetch m.user
            where m.tokenHash = :tokenHash and m.action = :action
            """)
    Optional<MailTokenEntity> findByTokenHashAndActionWithUser(
            @Param("tokenHash") String tokenHash,
            @Param("action") MailTokenAction action
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            delete from MailToken m
            where m.user.id = :userId and m.action = :action and m.usedAt is null
            """)
    int deleteUnusedByUserIdAndAction(
            @Param("userId") UUID userId,
            @Param("action") MailTokenAction action
    );
}
