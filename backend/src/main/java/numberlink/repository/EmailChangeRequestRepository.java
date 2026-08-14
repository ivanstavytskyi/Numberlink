package numberlink.repository;

import numberlink.entity.EmailChangeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface EmailChangeRequestRepository extends JpaRepository<EmailChangeRequestEntity, Long> {

    Optional<EmailChangeRequestEntity> findByUser_Id(UUID userId);

    @Query("SELECT r FROM EmailChangeRequest r JOIN FETCH r.user WHERE r.cancelTokenHash = :hash")
    Optional<EmailChangeRequestEntity> findByCancelTokenHash(@Param("hash") String hash);

    boolean existsByNewEmailIgnoreCaseAndExpiresAtAfterAndUser_IdNot(
            String newEmail,
            Instant expiresAfter,
            UUID userId
    );

    void deleteByUser_Id(UUID userId);
}
