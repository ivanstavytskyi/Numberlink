package numberlink.repository;

import numberlink.entity.LocalUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LocalUserRepository extends JpaRepository<LocalUserEntity, Long> {
    Optional<LocalUserEntity> findByUserId(UUID userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE LocalUser l SET l.encodedPassword = :encodedPassword WHERE l.user.id = :userId")
    int updateEncodedPassword(
            @Param("userId") UUID userId,
            @Param("encodedPassword") String encodedPassword
    );
}
