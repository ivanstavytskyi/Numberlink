package numberlink.repository;

import numberlink.entity.LocalUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocalUserRepository extends JpaRepository<LocalUserEntity, Long> {
    Optional<LocalUserEntity> findByUserId(UUID userId);
}
